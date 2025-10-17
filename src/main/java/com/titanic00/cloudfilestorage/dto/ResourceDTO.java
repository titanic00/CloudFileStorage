package com.titanic00.cloudfilestorage.dto;

import com.titanic00.cloudfilestorage.enumeration.ObjectType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceDTO {
    private String path;
    private String name;
    private String size;
    private ObjectType type;
}
