package spring_boot.dto;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private boolean completed;
}
