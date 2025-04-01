package com.lsh.schedulerdev.module.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.common.utils.password.PasswordUtils;
import com.lsh.schedulerdev.module.member.domain.model.Member;
import com.lsh.schedulerdev.module.member.dto.MemberAuthDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberCreateDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberSignInDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberUpdateDto;
import com.lsh.schedulerdev.module.member.dto.response.MemberDto;
import com.lsh.schedulerdev.module.member.exception.MemberException;
import com.lsh.schedulerdev.module.member.exception.MemberExceptionCode;
import com.lsh.schedulerdev.module.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
	public CommonResponse<MemberDto> saveMember(MemberCreateDto memberCreateDto) {
		if (memberRepository.existsByEmail(memberCreateDto.getEmail())) {
			throw new MemberException(MemberExceptionCode.ALREADY_EXIST_MEMBER);
		}

		Member savedMember = memberRepository.save(Member.builder()
			.name(memberCreateDto.getName())
			.email(memberCreateDto.getEmail())
			.password(PasswordUtils.encryptPassword(memberCreateDto.getPassword()))
			.build());

		MemberDto memberDto = MemberDto.from(savedMember);

		return CommonResponse.of("회원 가입 성공", memberDto);
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

		return MemberAuthDto.from(member);
	}

	/**
	 * 모든 유저 조회
	 *
	 * @param pageable 페이지 값
	 * @return Page에서 원하는 정보 값만 담은 List를 반환
	 */
	public CommonResponses<MemberDto> getAllMembers(Pageable pageable) {
		Page<MemberDto> memberDtoPage = memberRepository.findAllByOrderByModifiedAtDesc(pageable)
			.map(MemberDto::from);

		return CommonResponses.from("모든 일정 조회 성공", memberDtoPage);
	}

	/**
	 * 유저 수정
	 *
	 * @param memberId        유저 id
	 * @param memberUpdateDto 수정할 내용
	 * @return 유저 정보
	 */
	@Transactional
	public CommonResponse<MemberDto> updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
		Member member = findById(memberId);

		member.updateMember(memberUpdateDto.getName(), PasswordUtils.encryptPassword(memberUpdateDto.getPassword()));

		MemberDto memberDto = MemberDto.from(member);

		return CommonResponse.of("유저 수정 성공", memberDto);
	}

	/**
	 * 유저 삭제
	 *
	 * @param memberId 유저 id
	 * @return 삭제된 유저 정보
	 */
	@Transactional
	public CommonResponse<Long> deleteMember(Long memberId) {
		Member member = findById(memberId);

		memberRepository.delete(member);

		return CommonResponse.of("유저 삭제 성공", member.getId());
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
