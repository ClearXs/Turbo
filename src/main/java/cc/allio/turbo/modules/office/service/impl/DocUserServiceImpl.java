package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.turbo.modules.auth.provider.TurboUser;
import cc.allio.turbo.modules.office.documentserver.callbacks.CallbackEventBus;
import cc.allio.turbo.modules.office.documentserver.command.CommandManager;
import cc.allio.turbo.modules.office.documentserver.command.Result;
import cc.allio.turbo.modules.office.documentserver.command.ResultCode;
import cc.allio.turbo.modules.office.documentserver.command.requestinfo.DropArgs;
import cc.allio.turbo.modules.office.documentserver.command.requestinfo.EmptyArgs;
import cc.allio.turbo.modules.office.documentserver.command.requestinfo.ForceSaveArgs;
import cc.allio.turbo.modules.office.documentserver.vo.Track;
import cc.allio.turbo.modules.office.dto.DocumentDTO;
import cc.allio.turbo.modules.office.dto.OnlineDocUser;
import cc.allio.turbo.modules.office.dto.page.DocPageDTO;
import cc.allio.turbo.modules.office.entity.Doc;
import cc.allio.turbo.modules.office.entity.DocCooperator;
import cc.allio.turbo.modules.office.entity.DocCustomization;
import cc.allio.turbo.modules.office.entity.DocPermissionGroup;
import cc.allio.turbo.modules.office.mapper.DocUserMapper;
import cc.allio.turbo.modules.office.service.*;
import cc.allio.turbo.modules.office.vo.DocPermission;
import cc.allio.turbo.modules.office.vo.DocUser;
import cc.allio.turbo.modules.office.vo.DocVO;
import cc.allio.turbo.modules.system.entity.SysUser;
import cc.allio.turbo.modules.system.service.ISysUserService;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.JsonUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DocUserServiceImpl implements IDocUserService {

    private final IDocService docService;
    private final IDocPermissionGroupService permissionGroupService;
    private final IDocCooperatorService cooperatorService;
    private final IDocCustomizationService docCustomizationService;
    private final ISysUserService sysUserService;
    private final DocUserMapper docUserMapper;

    private final CommandManager commandManager;

    @Override
    public Boolean favoriteOfDocument(Long docId) throws BizException {
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        Long userId = currentUser.getUserId();
        return
                docCustomizationService.update(
                        new DocCustomization(),
                        Wrappers.<DocCustomization>lambdaUpdate()
                                .set(DocCustomization::getFavorite, true)
                                .eq(DocCustomization::getUserId, userId)
                                .eq(DocCustomization::getDocId, docId)
                );
    }

    @Override
    public Boolean cancelFavoriteOfDocument(Long docId) throws BizException {
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        Long userId = currentUser.getUserId();
        return docCustomizationService.update(
                new DocCustomization(),
                Wrappers.<DocCustomization>lambdaUpdate()
                        .set(DocCustomization::getFavorite, false)
                        .eq(DocCustomization::getUserId, userId)
                        .eq(DocCustomization::getDocId, docId)
        );
    }

    @Override
    public Boolean favorOfDocument(Long docId) throws BizException {
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        Long userId = currentUser.getUserId();
        return docCustomizationService.update(
                new DocCustomization(),
                Wrappers.<DocCustomization>lambdaUpdate()
                        .set(DocCustomization::getFavor, true)
                        .eq(DocCustomization::getUserId, userId)
                        .eq(DocCustomization::getDocId, docId)
        );
    }

    @Override
    public Boolean cancelFavorOfDocument(Long docId) throws BizException {
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        Long userId = currentUser.getUserId();
        return docCustomizationService.update(
                new DocCustomization(),
                Wrappers.<DocCustomization>lambdaUpdate()
                        .set(DocCustomization::getFavor, false)
                        .eq(DocCustomization::getUserId, userId)
                        .eq(DocCustomization::getDocId, docId)
        );
    }

    @Override
    public DocUser getDocUserByRequest(Long docId) throws BizException {
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        Long userId = currentUser.getUserId();
        String userName = currentUser.getNickname();

        DocUser docUser = new DocUser();
        docUser.setUserId(userId);
        docUser.setUsername(userName);
        Doc doc = docService.getById(docId);
        if (doc == null) {
            throw new BizException("not found document");
        }
        docUser.setDoc(doc);
        DocCooperator docCooperator =
                cooperatorService.getOne(Wrappers.<DocCooperator>lambdaQuery().eq(DocCooperator::getDocId, doc.getId()).eq(DocCooperator::getCooperator, userId));
        DocPermissionGroup permissionGroup;
        if (docCooperator == null) {
            // get default permission group from doc
            permissionGroup = permissionGroupService.selectDefaultPermissionGroup(docId);
            docCooperator = new DocCooperator();
            docCooperator.setDocId(docId);
            docCooperator.setCooperator(userId);
            docCooperator.setPermissionGroupId(permissionGroup.getId());
            // saves to new doc cooperator.
            cooperatorService.save(docCooperator);
        } else {
            permissionGroup = permissionGroupService.getById(docCooperator.getPermissionGroupId());
        }
        // set
        if (permissionGroup != null) {
            String permissionJson = permissionGroup.getPermission();
            DocPermission permission = JsonUtils.parse(permissionJson, DocPermission.class);
            docUser.setPermission(permission);
            docUser.setPermissionGroup(permissionGroup);
        }
        DocCustomization docCustomization = docCustomizationService.selectOneByDocIdAndUserId(docId, userId);
        if (docCustomization == null) {
            docCustomization = docCustomizationService.settingToFavorite(docId, userId, false);
        }
        docUser.setDocCustomization(docCustomization);
        return docUser;
    }

    @Override
    public List<DocumentDTO> searchMineDocument(String pattern) throws BizException {
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        DocPageDTO docPageDTO = new DocPageDTO();
        docPageDTO.setCreator(currentUser.getUserId());
        docPageDTO.setCollaborator(currentUser.getUserId());

        List<DocVO> docVOS = docUserMapper.searchUserDocList(docPageDTO);
        List<DocumentDTO> documentList = handleDocumentList(docVOS);

        return documentList.stream()
                .filter(doc -> doc.getTitle().contains(pattern) || doc.getCreateName().contains(pattern))
                .toList();
    }

    @Override
    public IPage<DocumentDTO> selectRecentlyDocument(DocPageDTO params) throws BizException {
        return selectUserDocument(params);
    }

    @Override
    public IPage<DocumentDTO> selectShareToMeDocument(DocPageDTO params) throws BizException {
        params.setShared(true);
        return selectUserDocument(params);
    }

    @Override
    public IPage<DocumentDTO> selectMineFavoriteDocument(DocPageDTO params) throws BizException {
        params.setFavorite(true);
        return selectUserDocument(params);
    }

    @Override
    public IPage<DocumentDTO> selectMineCreateDocument(DocPageDTO params) throws BizException {
        return selectUserDocument(params);
    }

    @Override
    public IPage<DocumentDTO> selectMineFavorDocument(DocPageDTO params) throws BizException {
        params.setFavor(true);
        return selectUserDocument(params);
    }

    @Override
    public List<OnlineDocUser> getOnlineDocUser(Long docId) throws BizException {
        Doc doc = docService.getById(docId);
        if (doc == null) {
            throw new BizException("not found document");
        }
        return getOnlineUserFromCommand(doc.getKey());
    }

    @Override
    public Boolean kickout(Long docId, List<Long> userIds) throws BizException {
        if (CollectionUtils.isEmpty(userIds)) {
            return true;
        }
        Doc doc = docService.getById(docId);
        if (doc == null) {
            throw new BizException("not found document");
        }
        List<String> users = userIds.stream().map(String::valueOf).toList();
        return dropUsers(doc.getKey(), users);
    }

    @Override
    public Boolean kickoutOthres(Long docId) throws BizException {
        Doc doc = docService.getById(docId);
        if (doc == null) {
            throw new BizException("not found document");
        }
        String docKey = doc.getKey();
        List<OnlineDocUser> onlineDocUser = getOnlineUserFromCommand(docKey);
        List<String> users = onlineDocUser.stream().map(OnlineDocUser::getUserId).map(String::valueOf).toList();
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        Collection<String> otherUsers = CollectionUtils.complement(users, Lists.newArrayList(currentUser.getUserId().toString()));
        return dropUsers(docKey, Lists.newArrayList(otherUsers));
    }

    @Override
    public Boolean kickoutAll(Long docId) throws BizException {
        Doc doc = docService.getById(docId);
        if (doc == null) {
            throw new BizException("not found document");
        }
        String docKey = doc.getKey();
        List<OnlineDocUser> onlineDocUser = getOnlineUserFromCommand(docKey);
        List<String> users = onlineDocUser.stream().map(OnlineDocUser::getUserId).map(String::valueOf).toList();
        return dropUsers(docKey, users);
    }

    /**
     * get online user from command callback
     *
     * @param docKey the doc user
     * @return the {@link OnlineDocUser} user list
     */
    List<OnlineDocUser> getOnlineUserFromCommand(String docKey) {
        CallbackEventBus.Subscriber subscribe = CallbackEventBus.subscribe();
        Result result = commandManager.info().execute(docKey, EmptyArgs.identifier);
        ResultCode code = result.getCode();
        if (code == ResultCode.noError) {
            Track track;
            try {
                track = subscribe.get(5L, TimeUnit.SECONDS);
            } catch (Throwable ex) {
                log.error("Failed to get online user from callback event bus, now return empty list", ex);
                return Collections.emptyList();
            }
            if (track == null) {
                return Collections.emptyList();
            }
            List<String> users = track.getUsers();

            if (CollectionUtils.isEmpty(users)) {
                return Collections.emptyList();
            }

            List<Long> userIds = users.stream().map(Long::valueOf).toList();
            List<SysUser> orgUserModels =
                    sysUserService.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getId, userIds));
            return orgUserModels.stream()
                    .map(user -> {
                        OnlineDocUser onlineDocUser = new OnlineDocUser();
                        onlineDocUser.setDocKey(docKey);
                        onlineDocUser.setUserId(user.getId());
                        onlineDocUser.setUserName(user.getNickname());
                        return onlineDocUser;
                    })
                    .toList();
        } else {
            CallbackEventBus.remove(subscribe);
        }
        return Collections.emptyList();
    }

    /**
     * drop users from document use by {@link CommandManager}
     *
     * @param docKey the doc key
     * @param users the user id list
     * @return success if true
     */
    Boolean dropUsers(String docKey, List<String> users) {
        DropArgs dropArgs = DropArgs.builder().users(users).build();
        Result result = commandManager.drop().execute(docKey, dropArgs);
        ResultCode code = result.getCode();
        return code == ResultCode.noError;
    }

    @Override
    public Boolean forceSave(Long docId) throws BizException {
        Doc doc = docService.getById(docId);
        if (doc == null) {
            throw new BizException("not found document");
        }
        kickoutAll(docId);
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        Long id = currentUser.getUserId();
        ForceSaveArgs args = ForceSaveArgs.builder().userdata(id.toString()).build();
        Result result = commandManager.forceSave().execute(doc.getKey(), args);
        return result.getCode() == ResultCode.noError;
    }

    /**
     * select mine document
     *
     * @param params the {@link DocPageDTO} instance
     * @return the {@link DocumentDTO} list
     */
    @Override
    public IPage<DocumentDTO> selectUserDocument(DocPageDTO params) throws BizException {
        TurboUser currentUser = AuthUtil.getCurrentUser();
        if (currentUser == null) {
            throw new BizException("current user not exist");
        }
        params.setCreator(currentUser.getUserId());
        params.setCollaborator(currentUser.getUserId());
        IPage<DocVO> docPageList = docUserMapper.selectUserDocList(params);
        List<DocVO> records = docPageList.getRecords();
        List<DocumentDTO> documentList = handleDocumentList(records);
        IPage<DocumentDTO> pageOfDocument = new Page<>();
        pageOfDocument.setTotal(docPageList.getTotal());
        pageOfDocument.setCurrent(docPageList.getCurrent());
        pageOfDocument.setSize(docPageList.getSize());
        pageOfDocument.setRecords(documentList);
        return pageOfDocument;
    }

    /**
     * handle {@link DocVO} data from database.
     * <p>replace user id to user name has creator and collaborator</p>
     *
     * @param records the page of {@link DocVO}
     * @return
     */
    List<DocumentDTO> handleDocumentList(List<DocVO> records) {
        // build user ids
        Set<Long> creatorList =
                records.stream().map(DocVO::getCreator).filter(Objects::nonNull).collect(Collectors.toSet());

        Set<Long> collaboratorList =
                records.stream().map(DocVO::getCooperator).filter(Objects::nonNull).collect(Collectors.toSet());

        Set<Long> userIdList = Sets.newHashSet();
        userIdList.addAll(creatorList);
        userIdList.addAll(collaboratorList);
        Map<Long, SysUser> idKeyUser = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            List<SysUser> orgUserModels =
                    sysUserService.list(Wrappers.<SysUser>lambdaQuery().in(SysUser::getId, userIdList));
            idKeyUser = orgUserModels.stream().collect(Collectors.toMap(SysUser::getId, k -> k));
        }

        Map<Long, SysUser> finalIdKeyUser = idKeyUser;
        return records.stream()
                .map(doc -> {
                    DocumentDTO documentDTO = new DocumentDTO();
                    BeanUtils.copyProperties(doc, documentDTO);

                    // set collaborator name
                    Long cooperator = documentDTO.getCooperator();
                    Optional.ofNullable(cooperator)
                            .map(finalIdKeyUser::get)
                            .map(SysUser::getNickname)
                            .ifPresent(documentDTO::setCollaboratorName);

                    // set creator name
                    Long creator = documentDTO.getCreator();
                    Optional.ofNullable(creator)
                            .map(finalIdKeyUser::get)
                            .map(SysUser::getNickname)
                            .ifPresent(documentDTO::setCreateName);
                    return documentDTO;
                })
                .toList();
    }

}