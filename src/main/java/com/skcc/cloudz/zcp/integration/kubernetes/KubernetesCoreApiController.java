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
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Namespace;
import io.kubernetes.client.models.V1NamespaceList;
import io.kubernetes.client.models.V1ServiceAccount;
import io.kubernetes.client.models.V1ServiceAccountList;
import io.kubernetes.client.util.Config;

@Component
public class KubernetesCoreApiController {
	private final Logger log = (Logger) LoggerFactory.getLogger(KubernetesCoreApiController.class);

	private ApiClient client;
	private CoreV1Api api;
	
	private String zcpSystemNamespace = "zcp-system";

	public KubernetesCoreApiController() throws IOException {
		ClassPathResource resource = new ClassPathResource("zcp-admin-03.conf");
		client = Config.fromConfig(resource.getInputStream());
		Configuration.setDefaultApiClient(client);

		log.info("kubernetes config's contents is {}", client);

		api = new CoreV1Api(client);
	}

	public V1NamespaceList getNamespaces() throws ApiException {
		List<Pair> labels = new ArrayList<Pair>();
		labels.add(LabelConfig.getSystemLabel());
		String labelSelector = LabelConfig.generateLabelSelectorString(labels);
		
		log.debug("labelSelector is {}", labelSelector);

		V1NamespaceList list = api.listNamespace("true", null, null, true, labelSelector, null, null, null, null);
		if (log.isDebugEnabled()) {
			for (V1Namespace item : list.getItems()) {
				log.debug("namespace is {}", item);
			}
		}

		return list;
	}

	public V1ServiceAccount createServiceAccount(V1ServiceAccount serviceAccount) throws ApiException {
		return api.createNamespacedServiceAccount(zcpSystemNamespace, serviceAccount, null);
	}

	public V1ServiceAccountList getServiceAccounts(String namespace) throws ApiException {
		List<Pair> labels = new ArrayList<Pair>();
//		labels.add(LabelConfig.getSystemLabel());
		labels.add(LabelConfig.getSystemUserLabel());
		String labelSelector = LabelConfig.generateLabelSelectorString(labels);

		log.debug("labelSelector is {}", labelSelector);
		
		return api.listNamespacedServiceAccount(zcpSystemNamespace, "true", null, null, null, labelSelector, null, null, null,
				null);
	}
}
