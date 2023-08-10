package com.ltj.myboard.service;

import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.repository.UserNotificationRepository;
import com.ltj.myboard.util.Ref;
import com.ltj.myboard.util.UserNotiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserNotiService {
    private final UserNotificationRepository userNotificationRepository;

    public List<UserNotification> getUserNotifications(String userId, PageRequest pageRequest, Ref<Integer> totalPageRef){
        Page<UserNotification> queryRet = userNotificationRepository.findAllByUserId(userId, pageRequest);
        List<UserNotification> retList = queryRet.getContent();
        totalPageRef.setValue(queryRet.getTotalPages());

        return retList;
    }

    public UserNotification getNotificationById(int notificationId){
        UserNotification found = userNotificationRepository.findById(notificationId).orElseThrow(() -> {
            throw new NoSuchElementException("cannot find " + notificationId + "Notification");
        });

        return found;
    }

    public void readNotification(UserNotification notification){
        notification.setRead(true);
        userNotificationRepository.save(notification);
    }

    private void setDefaultForNotification(UserNotification notification){
        notification.setRead(false);
        notification.setCreatedDay(new Date());
        notification.setModifyDay(new Date());
    }

    public UserNotification makeNotificationForComment(String senderId, String senderNickname, String userId, String content, int contentId){
        if(senderId.equals(userId)){
            return null;    // 알림을 발생시킨 유저와 알림을 받는 유저가 동일하면 알림을 생성하지 않는다.
        }
        UserNotification newNoti = new UserNotification();
        newNoti.setSenderId(senderId);
        newNoti.setSenderNickname(senderNickname);
        newNoti.setUserId(userId);
        newNoti.setContent(UserNotiUtil.makeContentForComment(senderNickname, content));
        newNoti.setContentId(contentId);
        newNoti.setContentType("comment");

        setDefaultForNotification(newNoti);
        userNotificationRepository.save(newNoti);

        return newNoti;
    }

    public UserNotification makeNotificationForSubComment(String senderId, String senderNickname, String userId, String content, int contentId){
        if(senderId.equals(userId)){
            return null;    // 알림을 발생시킨 유저와 알림을 받는 유저가 동일하면 알림을 생성하지 않는다.
        }
        UserNotification newNoti = new UserNotification();
        newNoti.setSenderId(senderId);
        newNoti.setSenderNickname(senderNickname);
        newNoti.setUserId(userId);
        newNoti.setContent(UserNotiUtil.makeContentForSubComment(senderNickname, content));
        newNoti.setContentId(contentId);
        newNoti.setContentType("comment");

        setDefaultForNotification(newNoti);
        userNotificationRepository.save(newNoti);

        return newNoti;
    }

    public UserNotification makeNotificationForLike(String senderId, String senderNickname, String userId, String content, int contentId){
        if(senderId.equals(userId)){
            return null;    // 알림을 발생시킨 유저와 알림을 받는 유저가 동일하면 알림을 생성하지 않는다.
        }
        UserNotification newNoti = new UserNotification();
        newNoti.setSenderId(senderId);
        newNoti.setSenderNickname(senderNickname);
        newNoti.setUserId(userId);
        newNoti.setContent(UserNotiUtil.makeContentForLikePost(senderNickname, content));
        newNoti.setContentId(contentId);
        newNoti.setContentType("post");

        setDefaultForNotification(newNoti);
        userNotificationRepository.save(newNoti);

        return newNoti;
    }

    public UserNotification makeNotificationForDisLike(String senderId, String senderNickname, String userId, String content, int contentId){
        if(senderId.equals(userId)){
            return null;    // 알림을 발생시킨 유저와 알림을 받는 유저가 동일하면 알림을 생성하지 않는다.
        }
        UserNotification newNoti = new UserNotification();
        newNoti.setSenderId(senderId);
        newNoti.setSenderNickname(senderNickname);
        newNoti.setUserId(userId);
        newNoti.setContent(UserNotiUtil.makeContentForDislikePost(senderNickname, content));
        newNoti.setContentId(contentId);
        newNoti.setContentType("post");

        setDefaultForNotification(newNoti);
        userNotificationRepository.save(newNoti);

        return newNoti;
    }
}
