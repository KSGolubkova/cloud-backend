package coursework.cloudstorage.service.mapper;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(target = "isFile", constant = "true")
    @Mapping(target = "size", source = "file.size")
    @Mapping(target = "folderId", source = "file.parent.id")
    @Mapping(target = "name", source = "file.name")
    @Mapping(target = "id", source = "file.id")
    StorageObjectDTO fileToStorageObjectDTO(File file);
}
