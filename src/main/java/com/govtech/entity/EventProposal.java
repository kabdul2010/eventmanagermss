
package com.govtech.entity;

import java.util.Date;

public interface EventProposal {
	Long getId();

	Long getEventId();

	String getPlaceName();

	String getProposedBy();

	Date getProposedOn();

}