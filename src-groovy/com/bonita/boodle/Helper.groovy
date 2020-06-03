package com.bonita.boodle

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import com.bonitasoft.engine.api.APIAccessor
import com.bonitasoft.engine.api.ProcessAPI;

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
}
