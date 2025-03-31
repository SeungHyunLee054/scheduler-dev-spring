package com.lsh.scheduler_dev.module.member.service;

import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.common.utils.password.PasswordUtils;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberCreateDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberSignInDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberUpdateDto;
import com.lsh.scheduler_dev.module.member.dto.response.MemberDto;
import com.lsh.scheduler_dev.module.member.exception.MemberException;
import com.lsh.scheduler_dev.module.member.exception.MemberExceptionCode;
import com.lsh.scheduler_dev.module.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입 성공")
    void success_saveMember() {
        // Given
        MemberCreateDto memberCreateDto = new MemberCreateDto("test", "test@test", "testtest");

        given(memberRepository.existsByEmail(any()))
                .willReturn(false);
        given(memberRepository.save(any()))
                .willReturn(getMember());

        // When
        MemberDto memberDto = memberService.saveMember(memberCreateDto);

        // Then
        verify(memberRepository, times(1)).save(any());
        assertAll(
                () -> assertEquals(1L, memberDto.getMemberId()),
                () -> assertEquals(memberCreateDto.getName(), memberDto.getName()),
                () -> assertEquals(memberCreateDto.getEmail(), memberDto.getEmail())
        );

    }

    @Test
    @DisplayName("회원 가입 실패 - 이미 존재하는 이메일")
    void fail_saveMember_alreadyExistEmail() {
        // Given
        MemberCreateDto memberCreateDto = new MemberCreateDto("test", "test@test", "testtest");

        given(memberRepository.existsByEmail(any()))
                .willReturn(true);

        // When
        MemberException exception =
                assertThrows(MemberException.class, () -> memberService.saveMember(memberCreateDto));

        // Then
        assertEquals(MemberExceptionCode.ALREADY_EXIST_MEMBER, exception.getErrorCode());

    }

    @Test
    @DisplayName("로그인 성공")
    void success_signIn() {
        // Given
        MemberSignInDto memberSignInDto = new MemberSignInDto("test@test", "testtest");

        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.ofNullable(getMember()));

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
        MemberSignInDto memberSignInDto = new MemberSignInDto("test@test", "testtest");

        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.empty());

        // When
        MemberException exception =
                assertThrows(MemberException.class, () -> memberService.signIn(memberSignInDto));

        // Then
        assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("모든 유저 조회 성공")
    void success_getAllMembers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Member member = getMember();
        List<Member> list = List.of(member, member);

        given(memberRepository.findAllByOrderByModifiedAtDesc(any()))
                .willReturn(new PageImpl<>(list, pageable, list.size()));

        // When
        ListResponse<MemberDto> result = memberService.getAllMembers(pageable);

        // Then
        List<MemberDto> content = result.getContent();
        for (MemberDto memberDto : content) {
            assertAll(
                    () -> assertEquals(memberDto.getMemberId(), member.getId()),
                    () -> assertEquals(memberDto.getEmail(), member.getEmail()),
                    () -> assertEquals(memberDto.getName(), member.getName())
            );
        }

    }

    @Test
    @DisplayName("유저 수정 성공")
    void success_updateMember() {
        // Given
        Member member = getMember();
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("t1", "test2test");

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(member));

        // When
        MemberDto memberDto = memberService.updateMember(1L, memberUpdateDto);

        // Then
        assertAll(
                () -> assertEquals(1L, memberDto.getMemberId()),
                () -> assertEquals(memberUpdateDto.getName(), memberDto.getName())
        );

    }

    @Test
    @DisplayName("유저 수정 실패 - 유저를 찾을 수 없음")
    void fail_updateMember_memberNotFound() {
        // Given
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto("t1", "test2test");

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // When
        MemberException exception =
                assertThrows(MemberException.class, () -> memberService.updateMember(1L, memberUpdateDto));

        // Then
        assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("유저 삭제 성공")
    void success_deleteMember() {
        // Given
        Member member = getMember();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        // When
        MemberDto memberDto = memberService.deleteMember(1L);

        // Then
        assertAll(
                () -> assertEquals(memberDto.getMemberId(), member.getId()),
                () -> assertEquals(memberDto.getName(), member.getName()),
                () -> assertEquals(memberDto.getEmail(), member.getEmail())
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
                assertThrows(MemberException.class, () -> memberService.deleteMember(1L));

        // Then
        assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }


    private Member getMember() {
        return Member.builder()
                .id(1L)
                .name("test")
                .email("test@test")
                .password(PasswordUtils.encryptPassword("testtest"))
                .build();
    }
}