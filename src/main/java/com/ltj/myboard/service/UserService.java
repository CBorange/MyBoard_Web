package com.ltj.myboard.service;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.domain.UserNotification;
import com.ltj.myboard.model.UserDetailsImpl;
import com.ltj.myboard.repository.UserNotificationRepository;
import com.ltj.myboard.repository.UserRepository;
import com.ltj.myboard.util.Ref;
import com.ltj.myboard.util.UserNotiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserNotificationRepository userNotificationRepository;

    public Optional<User> findUserByID(String ID)
    {
        return userRepository.findById(ID);
    }

    public List<UserNotification> getUserNotifications(String userId, PageRequest pageRequest, Ref<Integer> totalPageRef){
        Page<UserNotification> queryRet = userNotificationRepository.findAllByUserId(userId, pageRequest);
        List<UserNotification> retList = queryRet.getContent();
        totalPageRef.setValue(queryRet.getTotalPages());

        return retList;
    }

    public UserNotification getNotificationById(int notificationId){
        UserNotification found = userNotificationRepository.findById(notificationId).orElseThrow(() -> {
            throw new IllegalStateException("cannot find " + notificationId + "Notification");
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User found = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 사용자 입니다."));
        return new UserDetailsImpl(found);
    }
}
