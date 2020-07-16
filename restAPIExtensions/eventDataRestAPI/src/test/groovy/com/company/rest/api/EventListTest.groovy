package com.company.rest.api;

import groovy.json.JsonSlurper

import javax.servlet.http.HttpServletRequest

import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder

import spock.lang.Specification

import com.bonitasoft.web.extension.rest.RestAPIContext
import com.bonitasoft.engine.api.APIClient

import com.company.model.Event;
import com.company.model.EventDAO;
import com.company.model.Vote;
import com.company.model.VoteDAO;

import java.time.*;

/**
 * @see http://spockframework.github.io/spock/docs/1.0/index.html
 */
class EventListTest extends Specification {

    // Declare mocks here
    // Mocks are used to simulate external dependencies behavior
    def httpRequest = Mock(HttpServletRequest)
    def context = Mock(RestAPIContext)
	def apiClient = Mock(APIClient)
	def eventDAO = Mock(EventDAO)
	def voteDAO = Mock(VoteDAO)
	
	List<String> participants()
	{
		return ["0","1","2","3"];
	}
	List<Long> eventIds()
	{
		return [0];
	}
	List<LocalDate> dates()
	{
		return [LocalDate.of(2020,06,18)];
	}
	
	List<Event> virtualEvents()
	{
		Event e = new Event();
		e.setPersistenceId(eventIds()[0]);		
		e.setShortDescription("Helen birthday")
		e.setFullDescription("Let's have drink all together")
		e.setDates(dates());
		e.setCreatorId(participants()[0])
		e.setParticipantIds(participants())
		e.setFinalDate(LocalDate.of(2020,12,25))
		
		return [e];	
	}

    /**
     * You can configure mocks before each tests in the setup method
     */
    def setup(){
		eventDAO.find(_,_) >> {virtualEvents()}
        apiClient.getDAO(EventDAO.class) >> eventDAO
		context.getApiClient() >> apiClient
    }

	def should_return_error_when_unexisting_event()
	{
		given: "Loading our REST API extension with well known setup"
		when: "invoking the REST API with an unknown event"
		then: "an invalid event error should be received"
	}

    def should_return_a_json_representation_as_result()
	{
        given: "a RestAPIController"
        def api = new EventList()
        httpRequest.getParameter("u") >> 0;

        when: "Invoking the REST API"
        def apiResponse = api.doHandle(httpRequest, new RestApiResponseBuilder(), context)

        then: "A JSON representation is returned in response body"
        def jsonResponse = new JsonSlurper().parseText(apiResponse.response)
        apiResponse.httpStatus == 200
    }


}