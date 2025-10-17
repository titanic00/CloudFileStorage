package com.titanic00.cloudfilestorage.dto;

import com.titanic00.cloudfilestorage.enumeration.ObjectType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DirectoryDTO {
    private String path;
    private String name;
    private String size;
    private ObjectType type;
}
