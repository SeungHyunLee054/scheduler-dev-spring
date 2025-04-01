package com.lsh.scheduler_dev.module.member.service;

import com.lsh.scheduler_dev.common.response.ListResponse;
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
        MemberDto memberDto = memberService.saveMember(memberCreateDto);

        // Then
        verify(memberRepository, times(1)).save(any());
        assertAll(
                () -> assertEquals(1L, memberDto.getMemberId()),
                () -> assertEquals("test", memberDto.getName()),
                () -> assertEquals("test@test", memberDto.getEmail())
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

    @Test
    @DisplayName("모든 유저 조회 성공")
    void success_getAllMembers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Member> list = List.of(member, member);

        given(memberRepository.findAllByOrderByModifiedAtDesc(any()))
                .willReturn(new PageImpl<>(list, pageable, list.size()));

        // When
        ListResponse<MemberDto> result = memberService.getAllMembers(any());

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
        MemberDto memberDto = memberService.updateMember(anyLong(), memberUpdateDto);

        // Then
        assertAll(
                () -> assertEquals(1L, memberDto.getMemberId()),
                () -> assertEquals("test2", memberDto.getName())
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

    @Test
    @DisplayName("유저 삭제 성공")
    void success_deleteMember() {
        // Given
        given(member.getId())
                .willReturn(1L);
        given(member.getName())
                .willReturn("test");
        given(member.getEmail())
                .willReturn("test@test");

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        // When
        MemberDto memberDto = memberService.deleteMember(anyLong());

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
                assertThrows(MemberException.class, () -> memberService.deleteMember(anyLong()));

        // Then
        assertEquals(MemberExceptionCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

}