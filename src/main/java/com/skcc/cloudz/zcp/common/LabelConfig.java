package com.skcc.cloudz.zcp.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import com.skcc.cloudz.zcp.integration.kubernetes.KubernetesRbacAuthorizationApiController;

import ch.qos.logback.classic.Logger;
import io.kubernetes.client.Pair;

public class LabelConfig {
	private final static Logger log = (Logger) LoggerFactory.getLogger(KubernetesRbacAuthorizationApiController.class);

	public static Pair getSystemLabel() {
		return new Pair("iam.cloudzcp.io/zcp-system", "true");
	}

	public static Pair getSystemUserLabel() {
		return new Pair("iam.cloudzcp.io/user", "true");
	}

	public static Pair getSystemUsernameLabel(String username) {
		return new Pair("iam.cloudzcp.io/username", username);
	}

	public static String generateLabelSelectorString(List<Pair> labels) {
		if (labels == null || labels.isEmpty())
			return null;

		String labelSelector = null;
		StringBuilder builder = new StringBuilder();

		for (Pair pair : labels) {
			builder.append(pair.getName()).append("=").append(pair.getValue()).append(",");
		}

		labelSelector = builder.toString();
		labelSelector = StringUtils.removeEnd(labelSelector, ",");

		log.debug("labelSelector is {}", labelSelector);

		return labelSelector;
	}
}
