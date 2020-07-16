package com.bonita.boodle

import java.time.format.DateTimeFormatter;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import com.bonitasoft.engine.api.APIAccessor
import com.bonitasoft.engine.api.ProcessAPI;

import com.company.model.Event;

class Helper
{
	static def getInitiatorIdString(APIAccessor apiAccessor, Long processInstanceId)
	{
		ProcessAPI processApi = apiAccessor.getProcessAPI();
		IdentityAPI identityApi = apiAccessor.getIdentityAPI();
		ProcessInstance processInstance = processApi.getProcessInstance(processInstanceId)
		long initiator = processInstance.getStartedBy();
		String initiatorId = identityApi.getUser(initiator).id;
		
		return initiatorId;
	}
	
	static def eventToJson(Event e)
	{
		String finalDate = null;
		if (e.getFinalDate() != null)
		{
			finalDate = e.getFinalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
		}
		List<String> dates = [];
		e.getDates().forEach { t -> dates.add(t.format(DateTimeFormatter.ISO_LOCAL_DATE))};
		
		def outMap = ["shortDescription":e.getShortDescription(), 
			"fullDescription":e.getFullDescription(),
			"dates":dates,
			"creatorId":e.getCreatorId(),
			"participantIds":e.getParticipantIds(),
			"finalDate":finalDate];
		
		return outMap;
	}
}
