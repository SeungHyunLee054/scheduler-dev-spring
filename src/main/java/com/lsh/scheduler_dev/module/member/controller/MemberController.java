package com.lsh.scheduler_dev.module.member.controller;


import com.lsh.scheduler_dev.module.member.dto.*;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody MemberCreateRequestDto dto) {
        return ResponseEntity.ok(memberService.saveMember(dto));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody MemberSignInRequestDto dto,
                                    HttpServletRequest request
    ) {
        try {
            HttpSession session = request.getSession();
            MemberAuthDto memberAuthDto = memberService.signIn(dto);
            session.setAttribute("Authorization", memberAuthDto);

            return ResponseEntity.ok("logged in");
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable Long memberId,
            MemberAuthDto memberAuthDto,
            @RequestBody MemberUpdateRequestDto memberUpdateRequestDto
    ) {
        return ResponseEntity.ok(memberService.updateMember(memberId, memberAuthDto, memberUpdateRequestDto));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> deleteMember(
            @PathVariable Long memberId,
            @SessionAttribute(name = "Authorization") MemberAuthDto memberAuthDto,
            HttpServletRequest request
    ) {
        request.getSession().removeAttribute("Authorization");

        return ResponseEntity.ok(memberService.removeMember(memberId, memberAuthDto));
    }

}
