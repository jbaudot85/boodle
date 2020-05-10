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
class IndexTest extends Specification {

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
	List<LocalDate> dates()
	{
		return [LocalDate.of(2020,04,26),LocalDate.of(2020,04,27),LocalDate.of(2020,04,28)];
	}
	List<Boolean> attendance(Boolean b1,Boolean b2,Boolean b3)
	{
		return [b1,b2,b3];
	}
	
	Event virtualEvent(long eventID)
	{
		Event e = new Event();
		e.setPersistenceId(eventID);		
		e.setShortDescription("Helen birthday")
		e.setFullDescription("Let's have dring all together")
		e.setDates(dates());
		e.setCreatorId(participants()[0])
		e.setParticipantIds(participants())
		e.setFinalDate(LocalDate.of(2020,12,25))
		
		return e;	
	}
	
	List<Vote> virtualVotes(long eventId)
	{
		Vote v0 = new Vote();
		v0.setEventId(eventId)
		v0.setVoterId(participants()[0].toLong())
		v0.setDates(dates())
		v0.setAttendance(attendance(true,true,true))
		v0.setSubmitted(true);
		
		Vote v1 = new Vote();
		v1.setEventId(eventId)
		v1.setVoterId(participants()[1].toLong())
		v1.setDates(dates())
		v1.setAttendance(attendance(true,false,true))
		v1.setSubmitted(true);
		
		Vote v2 = new Vote();
		v2.setEventId(eventId)
		v2.setVoterId(participants()[2].toLong())
		v2.setDates(dates())
		v2.setAttendance(attendance(false,true,true))
		v2.setSubmitted(true);
		
		Vote v3 = new Vote();
		v3.setEventId(eventId)
		v3.setVoterId(participants()[3].toLong())
		v3.setDates(dates())
		v3.setAttendance(attendance(false,false,false))
		v3.setSubmitted(false);
		
		return [v0,v1,v2,v3];
	}
	static long refEventID = 1617;

    /**
     * You can configure mocks before each tests in the setup method
     */
    def setup(){
		eventDAO.findByPersistenceId(_) >> {virtualEvent(refEventID)}
		voteDAO.findByEventId(_,_,_) >> {virtualVotes(refEventID)}
        apiClient.getDAO(EventDAO.class) >> eventDAO
		apiClient.getDAO(VoteDAO.class) >> voteDAO
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
        def index = new Index()
        httpRequest.getParameter("e") >> this.refEventID.toString();

        when: "Invoking the REST API"
        def apiResponse = index.doHandle(httpRequest, new RestApiResponseBuilder(), context)

        then: "A JSON representation is returned in response body"
        def jsonResponse = new JsonSlurper().parseText(apiResponse.response)
        // Validate returned response
        apiResponse.httpStatus == 200
        //jsonResponse.p == "aValue1"
        //jsonResponse.myParameterKey == "testValue"
    }


}