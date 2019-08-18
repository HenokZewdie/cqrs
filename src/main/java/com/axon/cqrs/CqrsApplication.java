package com.axon.cqrs;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
//import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@SpringBootApplication
public class CqrsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CqrsApplication.class, args);
	}

	@RestController
	public static class ComplainAPI{
		
		private final ComplaintQueryObjectRepository repository;
		private final CommandGateway commandGateway;
		
		
		public ComplainAPI(ComplaintQueryObjectRepository repository, CommandGateway commandGateway) {
			this.repository = repository;
			this.commandGateway = commandGateway;
		}

		@PostMapping
		public CompletableFuture<String> fileComplaints(@RequestBody Map<String, String> request){
			String id = UUID.randomUUID().toString();
			return commandGateway.send(new FileComplaintCommand(id, request.get("company"), request.get("description")));
		}

		@GetMapping
		public List<ComplaintQueryObject> findAll(){
			return repository.findAll();
		}
		@GetMapping(value = "/{id}")
		public ComplaintQueryObject find(@PathVariable String id){
			return repository.findOne(id);
		}
	}
	@Aggregate
	public static class Complaint{
		@org.axonframework.commandhandling.model.AggregateIdentifier
		private String complaintId;
		
		@CommandHandler
		public Complaint(FileComplaintCommand complaintCommand){
			Assert.hasLength(complaintCommand.getCompany());
			
			apply(new ComplaintFileEvent(complaintCommand.getId(), complaintCommand.getCompany(), complaintCommand.getDescription()));
		}
		public Complaint(){
			
		}
		@EventSourcingHandler
		public void on(ComplaintFileEvent event){
			this.complaintId = event.getId();
		}
	}
	@Component
	public static class ComplaintQueryObjectUpdater{
		private final ComplaintQueryObjectRepository repo;
		
		
		public ComplaintQueryObjectUpdater(ComplaintQueryObjectRepository repo) {
			this.repo = repo;
		}
		@EventHandler
		public void on(ComplaintFileEvent event){
			repo.save(new ComplaintQueryObject(event.getId(), event.getCompany(), event.getDescription()));
		}
		
	}
	
	
	public static class FileComplaintCommand
	{
		private String id;
		private String company;
		private String description;
		
		public String getId() {
			return id;
		}

		public String getCompany() {
			return company;
		}

		public String getDescription() {
			return description;
		}

		public FileComplaintCommand(String id, String company, String description){
			
		}
	}
}
