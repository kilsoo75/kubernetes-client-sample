package com.skcc.cloudz.zcp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.skcc.cloudz.zcp.common.Response;
import com.skcc.cloudz.zcp.integration.kubernetes.KubernetesCoreApiController;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1ServiceAccountList;

@Configuration
@RestController
@RequestMapping("/core/")
public class CoreApiController {

	private static final Logger log = LoggerFactory.getLogger(CoreApiController.class);

	@Autowired
	private KubernetesCoreApiController kubernetesCoreApiController;

	@RequestMapping(value = "/serviceaccount", method = RequestMethod.GET)
	public Response<V1ServiceAccountList> getServiceAccounts() throws ApiException {
		Response<V1ServiceAccountList> response = new Response<V1ServiceAccountList>();
		response.setData(kubernetesCoreApiController.getServiceAccounts("zcp-system"));
		
		log.debug("Response = {}", response);
		
		return response;
	}

	@RequestMapping(value = "/namespace", method = RequestMethod.GET)
	public Response<V1NamespaceList> getNamespaces() throws ApiException {
		Response<V1NamespaceList> response = new Response<V1NamespaceList>();
		response.setData(kubernetesCoreApiController.getNamespaces());
		
		log.debug("Response = {}", response);
		
		return response;
	}
}
