package com.titanic00.cloudfilestorage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DirectoryDTO {
    private String path;
    private String name;
    private Long size;
    private String type;
}
