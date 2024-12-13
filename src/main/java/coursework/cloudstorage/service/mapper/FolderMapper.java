package coursework.cloudstorage.service.mapper;

import coursework.cloudstorage.model.dto.StorageObjectDTO;
import coursework.cloudstorage.model.entity.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FolderMapper {
    FolderMapper INSTANCE = Mappers.getMapper(FolderMapper.class);

    @Mapping(target = "isFile", constant = "false")
    @Mapping(target = "folderId", source = "folder.parent.id")
    @Mapping(target = "name", source = "folder.name")
    @Mapping(target = "id", source = "folder.id")
    StorageObjectDTO folderToStorageObjectDTO(Folder folder);
}
