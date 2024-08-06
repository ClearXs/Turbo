package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.uno.repository.impl.SimpleTurboCrudTreeRepositoryServiceImpl;
import cc.allio.turbo.modules.office.entity.Folder;
import cc.allio.turbo.modules.office.service.IFolderService;
import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl extends SimpleTurboCrudTreeRepositoryServiceImpl<Folder> implements IFolderService {
}
