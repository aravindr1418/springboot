package com.aravind.Springboot.customer;

import com.aravind.Springboot.exception.DuplicateResourceException;
import com.aravind.Springboot.exception.RequestValidationException;
import com.aravind.Springboot.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService    {

    private final CustomerDao customerDao;



    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCusotmers();
    }

    public Customer getCustomers(Integer id) {
        return customerDao.selectCusotmerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer with Id [%s] is not found".formatted(id)));
    }
    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exists
        String email = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)){
        throw new DuplicateResourceException("email already taken"
        );
    }
    //add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
                );
        customerDao.insertcustomer(customer);
    }
    //delete
    public void deleteCustomerById(Integer customerId){
        if(!customerDao.existsPersonWithId(customerId)){
            throw new ResourceNotFoundException(
                    "customer with id[%s] not found.".formatted(customerId)
            );
        }
        customerDao.deleteCustomerById(customerId);
    }
    public void updateCustomer(Integer customerId,
                               CustomerUpdateRequest updateRequest){
        Customer customer = getCustomers(customerId);

        boolean changes = false;

        if(updateRequest.name()!=null && !updateRequest.name().equals(customer.getName())){
            customer.setName(updateRequest.name());
            changes = true;
        }
        if(updateRequest.age()!= null && !updateRequest.age().equals(customer.getAge())){
            customer.setAge(updateRequest.age());
            changes = true;
        }
        if(updateRequest.email()!= null && !updateRequest.email().equals(customer.getEmail())){
            if(customerDao.existsPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceException("Email already exists");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }
        if(!changes){
            throw new RequestValidationException("no data changes found");
        }
        customerDao.updateCustomer(customer);
    }
}
