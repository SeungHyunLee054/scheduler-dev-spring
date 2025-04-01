package com.lsh.schedulerdev.module.member.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.lsh.schedulerdev.common.constants.SessionConstants;
import com.lsh.schedulerdev.common.constants.SessionExpiredConstant;
import com.lsh.schedulerdev.common.response.CommonResponse;
import com.lsh.schedulerdev.common.response.CommonResponses;
import com.lsh.schedulerdev.module.member.dto.MemberAuthDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberCreateDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberSignInDto;
import com.lsh.schedulerdev.module.member.dto.request.MemberUpdateDto;
import com.lsh.schedulerdev.module.member.dto.response.MemberDto;
import com.lsh.schedulerdev.module.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	private final MemberService memberService;
	private final SessionExpiredConstant sessionExpiredConstant;

	@PostMapping("/signup")
	public ResponseEntity<CommonResponse<MemberDto>> signUp(@Valid @RequestBody MemberCreateDto memberCreateDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(memberService.saveMember(memberCreateDto));
	}

	@PostMapping("/signin")
	public ResponseEntity<CommonResponse<String>> signIn(
		@Valid @RequestBody MemberSignInDto memberSignInDto,
		HttpServletRequest request
	) {
		HttpSession session = request.getSession();
		MemberAuthDto memberAuthDto = memberService.signIn(memberSignInDto);
		session.setAttribute(SessionConstants.AUTHORIZATION, memberAuthDto);
		session.setMaxInactiveInterval(sessionExpiredConstant.getSessionExpiredTime());

		return ResponseEntity.ok(CommonResponse.of("로그인 성공", session.getId()));
	}

	@GetMapping
	public ResponseEntity<CommonResponses<MemberDto>> getAllMembers(
		@RequestParam(defaultValue = "0") Integer pageIdx,
		@RequestParam(defaultValue = "10") Integer pageSize
	) {
		return ResponseEntity.ok(memberService.getAllMembers(PageRequest.of(pageIdx, pageSize)));
	}

	@PutMapping
	public ResponseEntity<CommonResponse<MemberDto>> updateMember(
		@SessionAttribute(name = SessionConstants.AUTHORIZATION) MemberAuthDto memberAuthDto,
		@Valid @RequestBody MemberUpdateDto memberUpdateDto
	) {
		return ResponseEntity.ok(memberService.updateMember(memberAuthDto.getMemberId(), memberUpdateDto));
	}

	@DeleteMapping
	public ResponseEntity<CommonResponse<MemberDto>> deleteMember(
		@SessionAttribute(name = SessionConstants.AUTHORIZATION) MemberAuthDto memberAuthDto,
		HttpServletRequest request
	) {
		request.getSession().removeAttribute(SessionConstants.AUTHORIZATION);

		return ResponseEntity.ok(memberService.deleteMember(memberAuthDto.getMemberId()));
	}

}
