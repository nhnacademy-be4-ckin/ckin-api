/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package store.ckin.api.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.ckin.api.eureka.actuator.ApplicationStatus;


/**
 * 어플리케이션 상태를 변경하는 컨트롤러입니다.
 *
 * @author 정승조
 * @version 2024. 02. 17.
 */

@RestController
@RequestMapping("/api/actuator/status")
public class ApplicationStatusController {

    private final ApplicationInfoManager applicationInfoManager;
    private final ApplicationStatus applicationStatus;

    public ApplicationStatusController(ApplicationInfoManager applicationInfoManager, ApplicationStatus applicationStatus) {
        this.applicationInfoManager = applicationInfoManager;
        this.applicationStatus = applicationStatus;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void stopStatus(){
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
        applicationStatus.stopService();
    }

}
