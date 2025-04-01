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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     *
     * @param memberCreateDto 가입하려는 유저 정보
     * @return 유저 정보
     */
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

        return MemberDto.from(savedMember);
    }

    /**
     * 로그인
     *
     * @param memberSignInDto 이메일과 비밀번호
     * @return 유저의 id와 email 값
     */
    @Transactional
    public MemberAuthDto signIn(MemberSignInDto memberSignInDto) {
        Member member = memberRepository.findByEmail(memberSignInDto.getEmail())
                .orElseThrow(() -> new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));

        member.checkPassword(memberSignInDto.getPassword());

        return MemberAuthDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .build();
    }

    /**
     * 모든 유저 조회
     *
     * @param pageable 페이지 값
     * @return Page에서 원하는 정보 값만 담은 List를 반환
     */
    public ListResponse<MemberDto> getAllMembers(Pageable pageable) {
        return ListResponse.from(memberRepository.findAllByOrderByModifiedAtDesc(pageable)
                .map(MemberDto::from));
    }

    /**
     * 유저 수정
     *
     * @param memberId        유저 id
     * @param memberUpdateDto 수정할 내용
     * @return 유저 정보
     */
    @Transactional
    public MemberDto updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = findById(memberId);

        member.updateMember(memberUpdateDto.getName(), PasswordUtils.encryptPassword(memberUpdateDto.getPassword()));

        return MemberDto.from(member);
    }

    /**
     * 유저 삭제
     *
     * @param memberId 유저 id
     * @return 삭제된 유저 정보
     */
    @Transactional
    public MemberDto deleteMember(Long memberId) {
        Member member = findById(memberId);

        memberRepository.delete(member);

        return MemberDto.from(member);
    }

    /**
     * 유저 조회
     *
     * @param memberId 유저 id
     * @return id 값으로 조회된 유저
     */
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionCode.MEMBER_NOT_FOUND));
    }

}
