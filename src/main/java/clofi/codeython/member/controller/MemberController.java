package clofi.codeython.member.controller;

import clofi.codeython.member.controller.response.MemberResponse;
import clofi.codeython.member.controller.response.RankingResponse;
import clofi.codeython.member.domain.request.CreateMemberRequest;
import clofi.codeython.member.domain.request.UpdateMemberRequest;
import clofi.codeython.member.service.MemberService;
import clofi.codeython.member.service.dto.CustomMemberDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/api/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody CreateMemberRequest createMemberRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.signUp(createMemberRequest));
    }

    @GetMapping("/api/users")
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal CustomMemberDetails userDetails) {
        String username = userDetails.getUsername();

        return ResponseEntity.ok(memberService.getMember(username));
    }

    @PatchMapping("/api/users")
    public ResponseEntity<Long> update(
            @AuthenticationPrincipal CustomMemberDetails userDetails,
            @RequestBody UpdateMemberRequest updateMemberRequest) {
        String username = userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.OK).body(memberService.update(username, updateMemberRequest));
    }

    @GetMapping("/api/ranking")
    public ResponseEntity<RankingResponse> ranking(
            @AuthenticationPrincipal CustomMemberDetails userDetails) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(memberService.ranking(username));
    }

}
