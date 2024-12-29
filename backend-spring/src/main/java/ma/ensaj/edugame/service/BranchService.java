package ma.ensaj.edugame.service;

import ma.ensaj.edugame.entity.Branch;
import ma.ensaj.edugame.entity.Level;
import ma.ensaj.edugame.repository.BranchRepository;
import ma.ensaj.edugame.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private  LevelRepository levelRepository;


    public List<Branch> getAllBranches() {
        return branchRepository.findAllWithLevels();
    }

    public Optional<Branch> getBranchById(Long id) {
        return branchRepository.findById(id);
    }

    public Branch saveBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }
    public List<Branch> getBranchesByLevelId(Long levelId) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new IllegalArgumentException("Level not found with id: " + levelId));
        return branchRepository.findByLevel(level);
    }
}
