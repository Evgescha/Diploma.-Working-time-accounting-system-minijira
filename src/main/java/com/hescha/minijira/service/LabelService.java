package com.hescha.minijira.service;

import com.hescha.minijira.model.Issue;
import com.hescha.minijira.model.Label;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.repository.LabelRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.hescha.minijira.controller.LabelController.MESSAGE;

@Service
public class LabelService extends CrudService<Label> {

    private final LabelRepository repository;
    private final ProjectService projectService;
    private final IssueService issueService;

    public LabelService(LabelRepository repository,
                        ProjectService projectService,
                        IssueService issueService) {
        super(repository);
        this.repository = repository;
        this.projectService = projectService;
        this.issueService = issueService;
    }


    public Label update(Long id, Label entity) {
        Label read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Label not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Label entity, Label read) {
        read.setName(entity.getName());
        read.setDescription(entity.getDescription());
        read.setIssues(entity.getIssues());
    }

    public Project delete(Long id, RedirectAttributes ra) {
        Label label = read(id);
        Project project = label.getProject();
        try {
            for (Issue issue : project.getIssues()) {
                if (issue.getLabels().contains(label)) {
                    issue.getLabels().remove(label);
                    issueService.update(issue);
                }
            }
            project.getLabels().remove(label);
            projectService.update(project);
            label.setProject(null);
            delete(label.getId());
            ra.addFlashAttribute(MESSAGE, "Removing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Removing failed");
        }
        return project;
    }
}
