package clofi.codeython.problem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import clofi.codeython.member.domain.Member;
import clofi.codeython.problem.domain.Problem;
import clofi.codeython.problem.domain.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {

	Optional<Record> findByProblem(Problem problem);

	List<Record> findAllByMemberOrderByUpdatedAtDesc(Member member);

	List<Record> findAllByProblemAndMemberOrderByCreatedAtDesc(Problem problem, Member member);

	List<Record> findAllByProblemAndMember(Problem problem, Member member);
}
