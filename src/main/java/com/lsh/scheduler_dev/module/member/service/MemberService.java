package com.lsh.scheduler_dev.module.member.service;


import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.common.utils.PasswordUtils;
import com.lsh.scheduler_dev.module.member.domain.model.Member;
import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberCreateDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberSignInDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberUpdateDto;
import com.lsh.scheduler_dev.module.member.dto.response.MemberDto;
import com.lsh.scheduler_dev.module.member.exception.MemberException;
import com.lsh.scheduler_dev.module.member.exception.MemberExceptionCode;
import com.lsh.scheduler_dev.module.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberDto saveMember(MemberCreateDto memberCreateDto) {
        if (memberRepository.existsByEmail(memberCreateDto.getEmail())) {
            throw new MemberException(MemberExceptionCode.ALREADY_EXIST_MEMBER);
        }

        Member savedMember = memberRepository.save(Member.builder()
                .name(memberCreateDto.getName())
                .email(memberCreateDto.getEmail())
                .password(PasswordUtils.encryptPassword(memberCreateDto.getPassword()))
                .build());

        return MemberDto.toDto(savedMember);
    }

    @Transactional
    public MemberAuthDto signIn(MemberSignInDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail())
                .filter(m -> PasswordUtils.matches(dto.getPassword(), m.getPassword()))
                .orElseThrow(() -> new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

        return MemberAuthDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .build();
    }

    public ListResponse<MemberDto> getAllMembers(Pageable pageable) {
        return ListResponse.toListResponse(memberRepository.findAllByOrderByModifiedAtDesc(pageable)
                .map(MemberDto::toDto));
    }

    @Transactional
    public MemberDto updateMember(Long memberId, MemberUpdateDto dto) {
        Member member = findById(memberId);

        member.updateMember(dto.getName(), PasswordUtils.encryptPassword(dto.getPassword()));

        return MemberDto.toDto(member);
    }

    @Transactional
    public MemberDto removeMember(Long memberId) {
        Member member = findById(memberId);

        memberRepository.delete(member);

        return MemberDto.toDto(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));
    }

}
