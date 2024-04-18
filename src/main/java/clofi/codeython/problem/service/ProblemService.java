package clofi.codeython.problem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clofi.codeython.member.domain.Member;
import clofi.codeython.member.repository.MemberRepository;
import clofi.codeython.problem.controller.response.AllProblemResponse;
import clofi.codeython.problem.controller.response.RecordResponse;
import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.domain.Record;
import clofi.codeython.problem.domain.request.BaseCodeRequest;
import clofi.codeython.problem.domain.request.CreateProblemRequest;
import clofi.codeython.problem.repository.LanguageRepository;
import clofi.codeython.problem.repository.ProblemRepository;
import clofi.codeython.problem.repository.RecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProblemService {
	private final ProblemRepository problemRepository;
	private final LanguageRepository languageRepository;
	private final RecordRepository recordRepository;
	private final MemberRepository memberRepository;

	public Long createProblem(CreateProblemRequest createProblemRequest) {
		if (problemRepository.existsByTitle(createProblemRequest.getTitle())) {
			throw new IllegalArgumentException("이미 만들어진 문제 제목입니다.");
		}

		Problem problem = problemRepository.save(createProblemRequest.toProblem());
		for (BaseCodeRequest baseCode : createProblemRequest.getBaseCodes()) {
			System.out.println(baseCode.getLanguage());
			languageRepository.save(
				createProblemRequest.toLanguage(problem, baseCode.getLanguage(), baseCode.getCode()));
		}

		return problem.getProblemNo();
	}

	public List<AllProblemResponse> getAllProblem() {
		List<Problem> problems = problemRepository.findAll();

		if (problems.isEmpty()) {
			throw new EntityNotFoundException("등록된 문제가 없습니다.");
		}

		return problems.stream().map(problem -> {
			Optional<Record> record = recordRepository.findByProblemNo(problem);
			return record.map(value -> AllProblemResponse
					.of(problem, value.getAccuracy(), true))
				.orElseGet(() -> AllProblemResponse.of(problem, 0, false));
		}).collect(Collectors.toList());

	}

	public List<RecordResponse> getRecord(String userName) {
		Member member = memberRepository.findByUsername(userName);
		return recordRepository.findAllByUserNoOrderByUpdatedAtDesc(member).stream()
			.map(record -> {
				Problem problem = problemRepository.findByProblemNo(record.getProblemNo().getProblemNo());
				return RecordResponse.of(record, problem.getTitle());
			}).collect(Collectors.toList());
	}
}
