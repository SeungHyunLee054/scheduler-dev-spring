package com.lsh.scheduler_dev.module.member.controller;

import com.lsh.scheduler_dev.common.security.constant.AuthorizationConstant;
import com.lsh.scheduler_dev.common.security.jwt.provieder.dto.MemberAuthDto;
import com.lsh.scheduler_dev.common.security.jwt.provieder.dto.TokenDto;
import com.lsh.scheduler_dev.module.member.dto.MemberCreateRequestDto;
import com.lsh.scheduler_dev.module.member.dto.MemberResponseDto;
import com.lsh.scheduler_dev.module.member.dto.MemberSignInRequestDto;
import com.lsh.scheduler_dev.module.member.dto.MemberUpdateRequestDto;
import com.lsh.scheduler_dev.module.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
                                    HttpServletResponse response
    ) {
        try {
            Date now = new Date();
            TokenDto tokenDto = memberService.signIn(dto, now);
            long expiration = memberService.getExpiration(tokenDto.getAccessToken());

            Cookie cookie = new Cookie(AuthorizationConstant.AUTHORIZATION_HEADER, tokenDto.getAccessToken());
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) expiration);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok(tokenDto);
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
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody MemberUpdateRequestDto memberUpdateRequestDto
    ) {
        return ResponseEntity.ok(memberService.updateMember(memberId, memberAuthDto, memberUpdateRequestDto));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> deleteMember(
            @PathVariable Long memberId,
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("empty", null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        return ResponseEntity.ok(memberService.removeMember(memberId, memberAuthDto));
    }

}
