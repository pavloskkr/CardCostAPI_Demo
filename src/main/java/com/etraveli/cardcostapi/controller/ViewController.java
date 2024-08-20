package com.etraveli.cardcostapi.controller;

import com.etraveli.cardcostapi.dto.CardCostResponse;
import com.etraveli.cardcostapi.exception.ExternalApiException;
import com.etraveli.cardcostapi.model.CardClearingCost;
import com.etraveli.cardcostapi.service.CardProcessingService;
import com.etraveli.cardcostapi.service.ClearingCostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ViewController {

    private final ClearingCostService clearingCostService;
    private final CardProcessingService cardProcessingService;

    public ViewController(ClearingCostService clearingCostService, CardProcessingService cardProcessingService) {
        this.clearingCostService = clearingCostService;
        this.cardProcessingService = cardProcessingService;
    }

    @GetMapping("/matrix")
    public String showMatrix(Model model) {
        Map<String, CardClearingCost> costs = clearingCostService.getAllClearingCosts();
        model.addAttribute("costs", costs);
        return "matrix";
    }


    @PostMapping("/matrix/add")
    public String addClearingCost(@RequestParam String countryCode, @RequestParam double clearingCost) {
        CardClearingCost newCost = new CardClearingCost(countryCode, clearingCost);
        clearingCostService.saveClearingCost(newCost);
        // redirect to matrix to see the updated data
        return "redirect:/matrix";
    }

    @PostMapping("/matrix/delete")
    public String deleteClearingCost(@RequestParam String countryCode) {
        clearingCostService.deleteClearingCost(countryCode);
        return "redirect:/matrix";
    }

    // Show a form with pre-filled data for updating
    @GetMapping("/matrix/edit")
    public String showEditForm(@RequestParam String countryCode, Model model) {
        CardClearingCost cost = clearingCostService.getClearingCost(countryCode);
        model.addAttribute("cost", cost);
        return "edit-matrix"; // This will render the edit form
    }

    // Handle the update request
    @PostMapping("/matrix/update")
    public String updateClearingCost(@RequestParam String countryCode, @RequestParam double clearingCost) {
        CardClearingCost updatedCost = new CardClearingCost(countryCode, clearingCost);
        clearingCostService.updateClearingCost(updatedCost);
        return "redirect:/matrix"; // Redirect back to the matrix view after updating
    }

    @GetMapping("/lookup")
    public String showLookupForm() {
        return "lookup";
    }

    @PostMapping("/lookup")
    public String lookupClearingCost(@RequestParam String cardNumber, Model model) {
        try {
            // Removing the dashes before processing PAN
            String cleanedPan = cardNumber.replace("-", "");
            CardCostResponse result = cardProcessingService.calculateClearingCost(cleanedPan);

            // Passing both the result and the formatted card number back to the view
            model.addAttribute("result", result);
            model.addAttribute("formattedCardNumber", cardNumber); // Keep the formatted version

        } catch (IllegalArgumentException | ExternalApiException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "lookup";
    }

}
