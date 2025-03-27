package com.lsh.scheduler_dev.module.member.service;

import com.lsh.scheduler_dev.common.security.jwt.JwtProvider;
import com.lsh.scheduler_dev.common.security.jwt.provieder.dto.MemberAuthDto;
import com.lsh.scheduler_dev.common.security.jwt.provieder.dto.TokenDto;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.dto.MemberCreateRequestDto;
import com.lsh.scheduler_dev.module.member.dto.MemberResponseDto;
import com.lsh.scheduler_dev.module.member.dto.MemberSignInRequestDto;
import com.lsh.scheduler_dev.module.member.dto.MemberUpdateRequestDto;
import com.lsh.scheduler_dev.module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberResponseDto saveMember(MemberCreateRequestDto dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            // exception
        }

        Member savedMember = memberRepository.save(Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build());

        return MemberResponseDto.toDto(savedMember);
    }

    public TokenDto signIn(MemberSignInRequestDto dto, Date nowDateTime) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .filter(m -> m.getPassword().equals(dto.getPassword()))
                .orElseThrow(null);

        return new TokenDto(jwtProvider.generateToken(MemberAuthDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .build(), nowDateTime));
    }

    public long getExpiration(String token) {
        return jwtProvider.getExpiration(token) / 1000;
    }

    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDto::toDto)
                .toList();
    }

    public MemberResponseDto updateMember(long memberId, MemberAuthDto memberAuthDto, MemberUpdateRequestDto dto) {
        if (memberId != memberAuthDto.getMemberId()) {
            //exception
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(null);
        member.updateName(dto.getName());
        member.updatePassword(dto.getPassword());
        Member updatedMember = memberRepository.save(member);

        return MemberResponseDto.toDto(updatedMember);
    }

    public MemberResponseDto removeMember(long memberId, MemberAuthDto memberAuthDto) {
        if (memberId != memberAuthDto.getMemberId()) {
            //exception
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(null);
        memberRepository.deleteById(memberId);

        return MemberResponseDto.toDto(member);
    }
}
