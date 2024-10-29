package com.example.go_travel.controller;

import com.example.go_travel.model.Tour;
import com.example.go_travel.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class TourController {

    @Autowired
    private TourService tourService;
    @GetMapping("/post")
    public String showPost() {
        return "post"; // Trả về tên template "post.html"
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login"; // Trả về tên template "post.html"
    }

    @PostMapping("/post")
    public String summitTour(@RequestParam("tour_name") String tourName,
                             @RequestParam("tour_location") String tourLocation,
                             @RequestParam("tour_contact") String tourContact,
                             @RequestParam("tour_description") String tourDescription,
                             @RequestParam("tour_image") MultipartFile file,
                             Model model, Locale locale) throws IOException {
        // save file images
        try {
            tourService.saveTour(tourName,tourLocation, tourContact,tourDescription,file);
            model.addAttribute("message", "Tải lên thành công!");
        } catch (IOException e) {
            model.addAttribute("message", "Tải lên thất bại!");
            e.printStackTrace();
        }
        byte[] imageUrl= file.getBytes();
        String base64Image = Base64.getEncoder().encodeToString(imageUrl);
        //Hien thi thong tin da luu
        model.addAttribute("tourName", tourName);
        model.addAttribute("tourLocation", tourLocation);
        model.addAttribute("tourContact", tourContact);
        model.addAttribute("tourDescription", tourDescription);
        model.addAttribute("tourImage", base64Image);
        return "tourDetails";
    }
    @GetMapping("/tourList")
    public String showTourList(Model model) {
        List<Tour> tours = tourService.getAllTour();
        model.addAttribute("tours", tours);  // Đảm bảo rằng tours được thêm vào model
        return "tourList";
    }

    @GetMapping("/imageUrl/{id}")
    public ResponseEntity<byte[]> getTourImageUrl(@PathVariable Long id) {
        Optional<Tour> tourOptional = tourService.getTourById(id);

        if (tourOptional.isPresent()) {
            Tour tour = tourOptional.get();
            byte[] imageUrl = tour.getImageUrl();  // Lấy dữ liệu BLOB từ cơ sở dữ liệu

            // Tạo headers cho loại ảnh (ví dụ: image/jpeg)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);  // Bạn có thể thay đổi thành IMAGE_PNG nếu ảnh là PNG
            return new ResponseEntity<>(imageUrl, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
