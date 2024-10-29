package com.example.go_travel.service;
import com.example.go_travel.model.Tour;
import com.example.go_travel.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;

    public Tour saveTour(String name, String location, String contact, String description, MultipartFile file) throws IOException {
        Tour tour = new Tour();
        tour.setName(name);
        tour.setLocation(location);
        tour.setContact(contact);
        tour.setDescription(description);
        tour.setImageUrl(file.getBytes());  // Lưu hình ảnh dưới dạng byte[]
        return tourRepository.save(tour);
    }

    public List<Tour> getAllTour() {
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }

}
