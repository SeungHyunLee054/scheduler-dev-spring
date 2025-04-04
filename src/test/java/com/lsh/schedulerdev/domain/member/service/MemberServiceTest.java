package com.lsh.schedulerdev.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.domain.member.code.MemberExceptionCode;
import com.lsh.schedulerdev.domain.member.code.MemberSuccessCode;
import com.lsh.schedulerdev.domain.member.dto.MemberAuthDto;
import com.lsh.schedulerdev.domain.member.dto.request.MemberCreateDto;
import com.lsh.schedulerdev.domain.member.dto.request.MemberSignInDto;
import com.lsh.schedulerdev.domain.member.dto.request.MemberUpdateDto;
import com.lsh.schedulerdev.domain.member.dto.response.MemberDto;
import com.lsh.schedulerdev.domain.member.entity.Member;
import com.lsh.schedulerdev.domain.member.exception.MemberException;
import com.lsh.schedulerdev.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
	@Mock
	private MemberRepository memberRepository;

	@Mock
	private MemberCreateDto memberCreateDto;

	@Mock
	private MemberSignInDto memberSignInDto;

	@Mock
	private MemberUpdateDto memberUpdateDto;

	@Mock
	private Member member;

	@InjectMocks
	private MemberService memberService;

	@Nested
	@DisplayName("회원 가입 테스트")
	class SaveMemberTest {
		@Test
		@DisplayName("회원 가입 성공")
		void success_saveMember() {
			// Given
			given(memberCreateDto.getName())
				.willReturn("test");
			given(memberCreateDto.getEmail())
				.willReturn("test@test");
			given(memberCreateDto.getPassword())
				.willReturn("testtest");

			given(memberRepository.existsByEmail(any()))
				.willReturn(false);

			given(member.getId())
				.willReturn(1L);
			given(member.getName())
				.willReturn("test");
			given(member.getEmail())
				.willReturn("test@test");

			given(memberRepository.save(any(Member.class)))
				.willReturn(member);

			// When
			CommonResponse<MemberDto> response = memberService.saveMember(memberCreateDto);

			// Then
			verify(memberRepository, times(1)).save(any());
			assertAll(
				() -> assertTrue(response.isSuccess()),
				() -> assertEquals(MemberSuccessCode.MEMBER_SIGN_UP_SUCCESS.getMessage(), response.getMessage()),
				() -> assertEquals(MemberSuccessCode.MEMBER_SIGN_UP_SUCCESS.getHttpStatus().value(),
					response.getStatus()),
				() -> assertEquals(1L, response.getResult().getMemberId()),
				() -> assertEquals("test", response.getResult().getName()),
				() -> assertEquals("test@test", response.getResult().getEmail())
			);

		}

		@Test
		@DisplayName("회원 가입 실패 - 이미 존재하는 이메일")
		void fail_saveMember_alreadyExistEmail() {
			// Given
			given(memberRepository.existsByEmail(any()))
				.willReturn(true);

			// When
			MemberException exception =
				assertThrows(MemberException.class, () -> memberService.saveMember(memberCreateDto));

			// Then
			assertEquals(MemberExceptionCode.ALREADY_EXIST_MEMBER, exception.getErrorCode());

		}
	}

	@Nested
	@DisplayName("로그인 테스트")
	class SignInTest {
		@Test
		@DisplayName("로그인 성공")
		void success_signIn() {
			// Given
			given(memberSignInDto.getEmail())
				.willReturn("test@test");
			given(memberSignInDto.getPassword())
				.willReturn("testtest");

			given(member.getEmail())
				.willReturn("test@test");

			given(memberRepository.findByEmail(any()))
				.willReturn(Optional.of(member));

			// When
			MemberAuthDto memberAuthDto = memberService.signIn(memberSignInDto);

			// Then
			assertAll(
				() -> assertEquals(memberSignInDto.getEmail(), memberAuthDto.getEmail())
			);

		}

		@Test
		@DisplayName("로그인 실패 - 유저를 찾을 수 없음")
		void fail_signIn_memberNotFound() {
			// Given
			given(memberRepository.findByEmail(any()))
				.willReturn(Optional.empty());

			// When
			MemberException exception =
				assertThrows(MemberException.class, () -> memberService.signIn(memberSignInDto));

			// Then
			assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

		}
	}

	@Nested
	@DisplayName("유저 조회 테스트")
	class GetMembersTest {
		@Test
		@DisplayName("모든 유저 조회 성공")
		void success_getAllMembers() {
			// Given
			Pageable pageable = PageRequest.of(0, 10);
			List<Member> list = List.of(member, member);

			given(memberRepository.findAllByOrderByModifiedAtDesc(any()))
				.willReturn(new PageImpl<>(list, pageable, list.size()));

			// When
			CommonResponses<MemberDto> responses = memberService.getAllMembers(any());

			// Then
			List<MemberDto> result = responses.getResult();
			for (MemberDto memberDto : result) {
				assertAll(
					() -> assertTrue(responses.isSuccess()),
					() -> assertEquals(MemberSuccessCode.MEMBER_READ_ALL_SUCCESS.getMessage(), responses.getMessage()),
					() -> assertEquals(MemberSuccessCode.MEMBER_READ_ALL_SUCCESS.getHttpStatus().value(),
						responses.getStatus()),
					() -> assertEquals(memberDto.getMemberId(), member.getId()),
					() -> assertEquals(memberDto.getEmail(), member.getEmail()),
					() -> assertEquals(memberDto.getName(), member.getName())
				);
			}

		}
	}

	@Nested
	@DisplayName("유저 수정 테스트")
	class UpdateMemberTest {
		@Test
		@DisplayName("유저 수정 성공")
		void success_updateMember() {
			// Given
			given(memberUpdateDto.getName())
				.willReturn("test2");
			given(memberUpdateDto.getPassword())
				.willReturn("testtest2");

			given(member.getId())
				.willReturn(1L);
			given(member.getName())
				.willReturn("test2");

			given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(member));

			// When
			CommonResponse<MemberDto> response = memberService.updateMember(anyLong(), memberUpdateDto);

			// Then
			assertAll(
				() -> assertTrue(response.isSuccess()),
				() -> assertEquals(MemberSuccessCode.MEMBER_UPDATE_SUCCESS.getMessage(), response.getMessage()),
				() -> assertEquals(MemberSuccessCode.MEMBER_UPDATE_SUCCESS.getHttpStatus().value(),
					response.getStatus()),
				() -> assertEquals(1L, response.getResult().getMemberId()),
				() -> assertEquals("test2", response.getResult().getName())
			);

		}

		@Test
		@DisplayName("유저 수정 실패 - 유저를 찾을 수 없음")
		void fail_updateMember_memberNotFound() {
			// Given
			given(memberRepository.findById(anyLong()))
				.willReturn(Optional.empty());

			// When
			MemberException exception =
				assertThrows(MemberException.class, () -> memberService.updateMember(anyLong(), memberUpdateDto));

			// Then
			assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

		}
	}

	@Nested
	@DisplayName("유저 삭제 테스트")
	class DeleteMemberTest {
		@Test
		@DisplayName("유저 삭제 성공")
		void success_deleteMember() {
			// Given
			given(member.getId())
				.willReturn(1L);

			given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(member));

			// When
			CommonResponse<Long> response = memberService.deleteMember(anyLong());

			// Then
			assertAll(
				() -> assertTrue(response.isSuccess()),
				() -> assertEquals(MemberSuccessCode.MEMBER_DELETE_SUCCESS.getMessage(), response.getMessage()),
				() -> assertEquals(MemberSuccessCode.MEMBER_DELETE_SUCCESS.getHttpStatus().value(),
					response.getStatus()),
				() -> assertEquals(response.getResult(), member.getId())
			);

		}

		@Test
		@DisplayName("유저 삭제 실패 - 유저를 찾을 수 없음")
		void fail_deleteMember_memberNotFound() {
			// Given
			given(memberRepository.findById(anyLong()))
				.willReturn(Optional.empty());

			// When
			MemberException exception =
				assertThrows(MemberException.class, () -> memberService.deleteMember(anyLong()));

			// Then
			assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

		}
	}

}
