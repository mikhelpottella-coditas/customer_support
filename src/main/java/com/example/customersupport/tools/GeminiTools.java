package com.example.customersupport.tools;

import com.example.customersupport.dto.response.ComplaintResponseDto;
import com.example.customersupport.dto.response.GenericResponse;
import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.Priority;
import com.example.customersupport.service.ChatService;
import com.example.customersupport.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GeminiTools {

    private final ComplaintService complaintService;
    private final ChatService chatService;

    @Tool(description = "get all the complaints under my control, and always send the data in the json format")
    public List<ComplaintResponseDto> getAllComplaints(
            @ToolParam(description = "set number of the page list that you want to see") int page,
            @ToolParam(description = "set the size of the page by this you can adjust the size of the page") int size,
            @ToolParam(description = "select on what bases we want to sort the list. only select the filed from the entity that you are trying to access") String sortBy,
            @ToolParam(description = "select how you want to sort the order of the list true means ascending and descending") boolean ascending,
            @ToolParam(description = "use this if you want to search through the complaint by category field in the complaint entity") String search,
            @ToolParam(description = "use when you want to filter the complaint based on the complaintStatus") ComplaintStatus filter) {

        return complaintService.getAllComplaints(page, size, sortBy, ascending, search, filter);
    }


    @Tool(description = "get one complaint by the id of the complaint, and always send the data in the json format")
    public ComplaintResponseDto getComplaintById(
            @ToolParam(description = "you must need this id of the complaint to get the complaint. so before fetching ask for the id from the user and then proceed") Long id) {
        return complaintService.getComplaintById(id);
    }


    @Tool(description = "update the status of the complaint, to perform this operation you must " +
            "ask the user to tell what is the update he want you to change then only proceed,")
    public GenericResponse updateStatus(
            @ToolParam(description = "this id is to tell which complaint is you are trying to access. so this is an important field") Long id, ComplaintStatus complaintStatus) {
        return complaintService.updateStatus(id, complaintStatus);
    }

    @Tool(description = "to change the priority of a complaint, do not proceed with out asking the user for the priority he want to set")
    public GenericResponse setPriority(
            @ToolParam(description = "this id is to tell which complaint is you are trying to access. so this is an important field") Long id,
            @ToolParam(description = "confirm the priority from the user and proceed and it should match the Priority enums that we have") Priority priority) {
        return complaintService.setPriority(id, priority);
    }

    @Tool(description = "send a message to the customer as an agent, telling them the process that we are going through to solve there problem")
    public GenericResponse msgSender(
            @ToolParam(description = "this id is to tell which complaint is you are trying to access. so this is an important field") Long complaintId,
            @ToolParam(description = "this message we can take from the user or you can decide what to send you self but before sending the get the conformation from the user and proceed") String msg){

        return chatService.msgSender(complaintId,msg);
    }

}
