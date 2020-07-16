package com.company.rest.api;

import groovy.json.JsonBuilder

import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.http.HttpHeaders
import org.bonitasoft.web.extension.ResourceProvider
import org.bonitasoft.web.extension.rest.RestApiResponse
import org.bonitasoft.web.extension.rest.RestApiResponseBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.bonitasoft.web.extension.rest.RestAPIContext
import com.bonitasoft.web.extension.rest.RestApiController

import com.company.model.Event;
import com.company.model.EventDAO;

//import com.bonita.boodle.Helper; TODO

class EventList implements RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventList.class)

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
			"finalDate":finalDate,
			"persistenceId":e.getPersistenceId()];
		
		return outMap;
	}
	
    @Override
    RestApiResponse doHandle(HttpServletRequest request, RestApiResponseBuilder responseBuilder, RestAPIContext context) {
        // To retrieve query parameters use the request.getParameter(..) method.
        // Be careful, parameter values are always returned as String values

        // Retrieve eventID parameter
        def userIDstr = request.getParameter "u"
        if (userIDstr == null) {
            return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter u is missing"}""")
        }
		
		// Convert to long
		def userID = 0;
		try
		{
			userID = userIDstr.toLong();
		}
		catch (Exception e)
		{
			return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "the parameter u is not a long"}""")
		}

        // Retrieve all events
		def eventDAO;
		try
		{
			eventDAO = context.getApiClient().getDAO(EventDAO.class);
		}
		catch (Exception e)
		{
			return buildResponse(responseBuilder, HttpServletResponse.SC_BAD_REQUEST,"""{"error" : "unable to get the EventDAO"}""")
		}
        List<Event> allEvents = eventDAO.find(0, Integer.MAX_VALUE);
		
		
		// Keep events
		
		def keptEvents = [];
		for (Event event : allEvents)
		{
			if (event.creatorId.toLong() == userID || event.participantIds.contains(userIDstr))
				keptEvents.add(event);
		}
		
		
		def jsonEvents = [];
		keptEvents.forEach{e-> jsonEvents.add(eventToJson(e))};
		def result = ["events":jsonEvents];

        // Send the result as a JSON representation
        // You may use buildPagedResponse if your result is multiple
        return buildResponse(responseBuilder, HttpServletResponse.SC_OK, new JsonBuilder((Map)result).toString())
    }

    /**
     * Build an HTTP response.
     *
     * @param  responseBuilder the Rest API response builder
     * @param  httpStatus the status of the response
     * @param  body the response body
     * @return a RestAPIResponse
     */
    RestApiResponse buildResponse(RestApiResponseBuilder responseBuilder, int httpStatus, Serializable body) {
        return responseBuilder.with {
            withResponseStatus(httpStatus)
            withResponse(body)
            build()
        }
    }

    /**
     * Returns a paged result like Bonita BPM REST APIs.
     * Build a response with a content-range.
     *
     * @param  responseBuilder the Rest API response builder
     * @param  body the response body
     * @param  p the page index
     * @param  c the number of result per page
     * @param  total the total number of results
     * @return a RestAPIResponse
     */
    RestApiResponse buildPagedResponse(RestApiResponseBuilder responseBuilder, Serializable body, int p, int c, long total) {
        return responseBuilder.with {
            withContentRange(p,c,total)
            withResponse(body)
            build()
        }
    }

    /**
     * Load a property file into a java.util.Properties
     */
    Properties loadProperties(String fileName, ResourceProvider resourceProvider) {
        Properties props = new Properties()
        resourceProvider.getResourceAsStream(fileName).withStream { InputStream s ->
            props.load s
        }
        props
    }

}
