package com.notification.api.rest;


import com.notification.api.constant.ApiPaths;
import com.notification.api.dto.NotificationPaginationResponse;
import com.notification.core.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(ApiPaths.NOTIFICATION)
    public NotificationPaginationResponse getNotifications(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean isSortedByDateDescending
    ) {
        pageNumber = pageNumber > 0 ? pageNumber : NotificationService.DEFAULT_PAGE_NUMBER;
        pageSize = pageSize > 0 ? pageSize : NotificationService.DEFAULT_PAGE_SIZE;
        if (isSortedByDateDescending == null){
            isSortedByDateDescending = true;
        }
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return notificationService.getNotifications(userId, pageNumber, pageSize, query, isSortedByDateDescending);
    }

    @GetMapping(ApiPaths.NOTIFICATION_UNREAD_COUNT)
    public Integer getUnreadNotificationsCount(){
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return notificationService.getUnreadNotificationsCount(userId);
    }

    @PostMapping(ApiPaths.NOTIFICATION_READ)
    public void readNotifications(@RequestParam(value = "notificationIds") List<UUID> notificationIds){
        notificationService.readNotifications(notificationIds);
    }
}
