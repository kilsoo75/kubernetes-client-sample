package com.skcc.cloudz.zcp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.skcc.cloudz.zcp.common.Response;
import com.skcc.cloudz.zcp.integration.kubernetes.KubernetesRbacAuthorizationApiController;

import io.kubernetes.client.ApiException;
import io.kubernetes.client.models.V1ClusterRoleBinding;
import io.kubernetes.client.models.V1ClusterRoleBindingList;
import io.kubernetes.client.models.V1ClusterRoleList;

@Configuration
@RestController
@RequestMapping("/authz/")
public class RbacAuthzApiController {

	private static final Logger log = LoggerFactory.getLogger(RbacAuthzApiController.class);

	@Autowired
	private KubernetesRbacAuthorizationApiController kubernetesRbacAuthorizationApiController;

	@RequestMapping(value = "/clusterrole", method = RequestMethod.GET)
	public Response<V1ClusterRoleList> getClusterRoles() throws ApiException {
		Response<V1ClusterRoleList> response = new Response<V1ClusterRoleList>();
		response.setData(kubernetesRbacAuthorizationApiController.getClusterRoles());
		
		log.debug("Response = {}", response);
		
		return response;
	}

	@RequestMapping(value = "/clusterrolebinding", method = RequestMethod.GET)
	public Response<V1ClusterRoleBindingList> getClusterRoleBindings() throws ApiException {
		Response<V1ClusterRoleBindingList> response = new Response<V1ClusterRoleBindingList>();
		response.setData(kubernetesRbacAuthorizationApiController.getClusterRoleBidings(null));
		
		log.debug("Response = {}", response);
		
		return response;
	}
	
	@RequestMapping(value = "/clusterrolebinding/{username}", method = RequestMethod.GET)
	public Response<V1ClusterRoleBinding> getClusterRoleBindingsByUsername(@PathVariable(name="username") String username) throws ApiException {
		Response<V1ClusterRoleBinding> response = new Response<V1ClusterRoleBinding>();
		response.setData(kubernetesRbacAuthorizationApiController.getClusterRoleBindingByUsername(username));
		
		log.debug("Response = {}", response);
		
		return response;
	}
}
