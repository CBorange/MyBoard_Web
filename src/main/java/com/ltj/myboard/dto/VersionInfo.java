package com.ltj.myboard.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class VersionInfo {
    private String deployEnvironment;
    private String buildVersion;
    private String serverConnectUrl;
}
