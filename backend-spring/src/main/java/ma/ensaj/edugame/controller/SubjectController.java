package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Branch;
import ma.ensaj.edugame.entity.Subject;
import ma.ensaj.edugame.service.BranchService;
import ma.ensaj.edugame.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private BranchService branchService;

    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<Subject>> getSubjectsByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(subjectService.getSubjectsByBranch(branchId));
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        // Validate that the branch field is present and contains an ID
        if (subject.getBranch() == null || subject.getBranch().getId() == null) {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request if branch or branch.id is missing
        }

        // Fetch the Branch entity from the database
        Branch branch = branchService.getBranchById(subject.getBranch().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid branch ID"));

        // Associate the fetched Branch with the Subject
        subject.setBranch(branch);

        // Save the Subject and return the response
        Subject savedSubject = subjectService.saveSubject(subject);
        return ResponseEntity.ok(savedSubject);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @RequestBody Subject updatedSubject) {
        if (updatedSubject.getBranch() == null || updatedSubject.getBranch().getId() == null) {
            return ResponseEntity.badRequest().body(null); // Ensure branch ID is provided
        }

        // Fetch the branch from the database
        Branch branch = branchService.getBranchById(updatedSubject.getBranch().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid branch ID"));

        // Update the subject
        return subjectService.getSubjectById(id)
                .map(subject -> {
                    subject.setName(updatedSubject.getName());
                    subject.setBranch(branch); // Re-associate the subject with the branch
                    return ResponseEntity.ok(subjectService.saveSubject(subject));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
