package com.example.demo.Repositories;

import com.example.demo.Domain.UnconfirmedBasket;
import com.example.demo.Domain.VkLink;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VkLinkRepository extends CrudRepository<VkLink, Integer> {
    List<VkLink> findById(int id);
    List<VkLink> findAll();
}