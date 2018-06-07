package com.skcc.cloudz.zcp.integration.kubernetes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.skcc.cloudz.zcp.common.LabelConfig;

import ch.qos.logback.classic.Logger;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.Pair;
import io.kubernetes.client.apis.RbacAuthorizationV1Api;
import io.kubernetes.client.models.V1ClusterRole;
import io.kubernetes.client.models.V1ClusterRoleBinding;
import io.kubernetes.client.models.V1ClusterRoleBindingList;
import io.kubernetes.client.models.V1ClusterRoleList;
import io.kubernetes.client.util.Config;

@Component
public class KubernetesRbacAuthorizationApiController {
	private final Logger log = (Logger) LoggerFactory.getLogger(KubernetesRbacAuthorizationApiController.class);

	private ApiClient client;
	private RbacAuthorizationV1Api api;

	public KubernetesRbacAuthorizationApiController() throws IOException {
		ClassPathResource resource = new ClassPathResource("zcp-admin-03.conf");
		client = Config.fromConfig(resource.getInputStream());
		Configuration.setDefaultApiClient(client);

		log.info("kubernetes config's contents is {}", client);

		api = new RbacAuthorizationV1Api(client);
	}

	public V1ClusterRoleBindingList getClusterRoleBidings() throws ApiException {
		return getClusterRoleBidings(null);
	}
	
	public V1ClusterRoleBindingList getClusterRoleBidings(List<Pair> labels) throws ApiException {
		if (labels != null) {
//			labels.add(LabelConfig.getSystemLabel());
			labels.add(LabelConfig.getSystemUserLabel());
		} else {
			labels = new ArrayList<Pair>();
//			labels.add(LabelConfig.getSystemLabel());
			labels.add(LabelConfig.getSystemUserLabel());
		}

		String labelSelector = LabelConfig.generateLabelSelectorString(labels);

		V1ClusterRoleBindingList list = api.listClusterRoleBinding("true", null, null, null, labelSelector, null, null,
				null, null);

		if (log.isDebugEnabled()) {
			for (V1ClusterRoleBinding item : list.getItems()) {
				log.debug("clusterrolebinding is {}", item);
			}
		}

		return list;
	}

	public V1ClusterRoleBinding getClusterRoleBindingByUsername(String username) throws ApiException {
		V1ClusterRoleBinding userClusterRoleBinding = null;

		List<Pair> labels = new ArrayList<Pair>();
		// labels.add(LabelConfig.getSystemLabel());
		labels.add(LabelConfig.getSystemUserLabel());
		labels.add(LabelConfig.getSystemUsernameLabel(username));

		V1ClusterRoleBindingList list = getClusterRoleBidings(labels);
		if (list.getItems() == null || list.getItems() == null || list.getItems().isEmpty()) {
			throw new ApiException("user clusterrolebinding does not exist");
		} else {
			if (list.getItems().size() > 1) {
				throw new ApiException("user has clusterrolebinding more than one");
			}

			userClusterRoleBinding = list.getItems().get(0);
		}

		return userClusterRoleBinding;
	}
	
	public V1ClusterRoleList getClusterRoles() throws ApiException {
		return getClusterRoles(null);
		
	}

	public V1ClusterRoleList getClusterRoles(List<Pair> labels) throws ApiException {
		if (labels != null) {
			labels.add(LabelConfig.getSystemLabel());
		} else {
			labels = new ArrayList<Pair>();
			labels.add(LabelConfig.getSystemLabel());
		}

		String labelSelector = LabelConfig.generateLabelSelectorString(labels);

		V1ClusterRoleList list = api.listClusterRole("true", null, null, null, labelSelector, null, null, null, null);

		if (log.isDebugEnabled()) {
			for (V1ClusterRole item : list.getItems()) {
				log.debug("clusterrole is {}", item);
			}
		}

		return list;
	}
}
