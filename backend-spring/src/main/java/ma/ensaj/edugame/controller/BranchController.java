package ma.ensaj.edugame.controller;

import ma.ensaj.edugame.entity.Branch;
import ma.ensaj.edugame.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@CrossOrigin(origins = "http://localhost:3000")

public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public List<Branch> getAllBranches() {
        return branchService.getAllBranches();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        return branchService.getBranchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Branch createBranch(@RequestBody Branch branch) {
        return branchService.saveBranch(branch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch updatedBranch) {
        return branchService.getBranchById(id)
                .map(branch -> {
                    branch.setName(updatedBranch.getName());
                    return ResponseEntity.ok(branchService.saveBranch(branch));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/level/{levelId}")
    public ResponseEntity<List<Branch>> getBranchesByLevel(@PathVariable Long levelId) {
        List<Branch> branches = branchService.getBranchesByLevelId(levelId);
        return ResponseEntity.ok(branches);
    }
}
