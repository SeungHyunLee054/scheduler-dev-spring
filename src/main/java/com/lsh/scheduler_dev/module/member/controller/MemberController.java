package com.lsh.scheduler_dev.module.member.controller;


import com.lsh.scheduler_dev.common.constants.SessionConstants;
import com.lsh.scheduler_dev.common.constants.SessionExpiredConstant;
import com.lsh.scheduler_dev.common.response.ListResponse;
import com.lsh.scheduler_dev.module.member.dto.MemberAuthDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberCreateDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberSignInDto;
import com.lsh.scheduler_dev.module.member.dto.request.MemberUpdateDto;
import com.lsh.scheduler_dev.module.member.dto.response.MemberDto;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final SessionExpiredConstant sessionExpiredConstant;

    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signUp(@Valid @RequestBody MemberCreateDto memberCreateDto) {
        return ResponseEntity.ok(memberService.saveMember(memberCreateDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody MemberSignInDto memberSignInDto,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        MemberAuthDto memberAuthDto = memberService.signIn(memberSignInDto);
        session.setAttribute(SessionConstants.AUTHORIZATION, memberAuthDto);
        session.setMaxInactiveInterval(sessionExpiredConstant.getSessionExpiredTime());

        return ResponseEntity.ok("logged in");
    }

    @GetMapping
    public ResponseEntity<ListResponse<MemberDto>> getAllMembers(
            @RequestParam(defaultValue = "0") Integer pageIdx,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(memberService.getAllMembers(PageRequest.of(pageIdx, pageSize)));
    }

    @PutMapping
    public ResponseEntity<MemberDto> updateMember(
            @SessionAttribute(name = SessionConstants.AUTHORIZATION, required = false) MemberAuthDto memberAuthDto,
            @Valid @RequestBody MemberUpdateDto memberUpdateDto
    ) {
        return ResponseEntity.ok(memberService.updateMember(memberAuthDto.getMemberId(), memberUpdateDto));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberDto> deleteMember(
            @PathVariable Long memberId,
            @SessionAttribute(name = SessionConstants.AUTHORIZATION, required = false) MemberAuthDto memberAuthDto,
            HttpServletRequest request
    ) {
        request.getSession().removeAttribute(SessionConstants.AUTHORIZATION);

        return ResponseEntity.ok(memberService.removeMember(memberId, memberAuthDto.getMemberId()));
    }

}
