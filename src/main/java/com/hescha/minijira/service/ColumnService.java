package com.hescha.minijira.service;

import com.hescha.minijira.model.Column;
import com.hescha.minijira.model.Project;
import com.hescha.minijira.repository.ColumnRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
public class ColumnService extends CrudService<Column> {
    public static final String MESSAGE = "message";

    private final ProjectService projectService;

    public ColumnService(ColumnRepository repository, ProjectService projectService) {
        super(repository);
        this.projectService = projectService;
    }

    public Column update(Long id, Column entity) {
        Column read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity Column not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(Column entity, Column read) {
        read.setName(entity.getName());
        read.setIssues(entity.getIssues());
    }

    public Project delete(Long id, RedirectAttributes ra) {
        Column column = read(id);
        Project project = column.getProject();

        if (!column.getIssues().isEmpty()) {
            ra.addFlashAttribute(MESSAGE, "Cannot remove the column. It contains issues.");
        }

        try {
            project.getColumns().remove(column);
            projectService.update(project);
            column.setProject(null);
            delete(column.getId());
            ra.addFlashAttribute(MESSAGE, "Removing is successful");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute(MESSAGE, "Removing failed");
        }
        return project;
    }
}
