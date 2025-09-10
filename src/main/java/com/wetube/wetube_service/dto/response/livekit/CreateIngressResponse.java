package com.wetube.wetube_service.dto.response.livekit;

import livekit.LivekitIngress;
import lombok.Data;

@Data
public class CreateIngressResponse {
    private LivekitIngress.IngressInfo ingress;
    private String authToken;
    private ConnectionDetails connectionDetails;
}
