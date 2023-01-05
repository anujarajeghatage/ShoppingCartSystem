package com.shopping.walletservice.service;

import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import com.shopping.walletservice.dao.StatementRepository;
import com.shopping.walletservice.dao.WalletRepository;
import com.shopping.walletservice.entity.Statement;
import com.shopping.walletservice.entity.Wallet;
import com.shopping.walletservice.entity.WalletRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final StatementRepository statementRepository;

    public List<Wallet> getAllWallet() {
        return walletRepository.findAll();
    }

    public Wallet createWallet(Wallet wallet) { 
            wallet.setWalletId(getNextId());
            return walletRepository.save(wallet); 
    }

    @Synchronized
    public void addMoney(WalletRequest request)  {
        Optional<Wallet> byWalletId = walletRepository.findByWalletId(request.getWalletId());
        if (byWalletId.isPresent()) {
            Wallet wallet1 = byWalletId.get(); 
            wallet1.setCurrentBalance(wallet1.getCurrentBalance() + request.getAmount());
            walletRepository.save(wallet1);
        }else { 
        throw new NoSuchElementException("Wallet not found");
        }
    }
    @Synchronized
   	public void payMoney(WalletRequest wallet) {
   		 Optional<Wallet> byWalletId = walletRepository.findByWalletId(wallet.getWalletId());
   	        if (byWalletId.isPresent()) {
   	            Wallet wallet1 = byWalletId.get();
   	            if (wallet1.getCurrentBalance() < wallet.getAmount()) {
                       throw new IllegalArgumentException("Insufficient balance");
                   }
                   wallet1.setCurrentBalance(wallet1.getCurrentBalance() - wallet.getAmount());
                   walletRepository.save(wallet1);
   	        }else {
   	        	throw new NoSuchElementException("Wallete not found");
   	        }
   	}

    public Wallet findByWalletId(int walletId) {
    	Optional<Wallet> wallet = walletRepository.findByWalletId(walletId);
    	if(wallet.isEmpty())
            throw new NoSuchElementException("Wallet not found");
        return wallet.get();
    }

    public Wallet findByCustomerId(String customerId) {
    	Optional<Wallet> wallet = walletRepository.findByCustomerId(customerId);
    	if(wallet.isEmpty())
    		 throw new NoSuchElementException("Wallet not found");
        return wallet.get();
    }

    public List<Statement> getStatementForWallet(int walletId) {
    	List<Statement>  statements = statementRepository.findByWalletId(walletId);
    	if(statements.isEmpty())
    		 throw new NoSuchElementException("statements not found");
        return statements; 
    }

    public List<Statement> getAllStatemet() {
    	List<Statement> statements = statementRepository.findAll();
    	if(statements.isEmpty())
    		 throw new NoSuchElementException("statements not found");
        return statements;  
    }

    @Synchronized
    public void deleteWallet(int walletId) {
        Optional<Wallet> byWalletId = walletRepository.findByWalletId(walletId);
        if (byWalletId.isEmpty())
        	throw new NoSuchElementException("Wallete not found");
        	
        List<Statement> byWalletId1 = statementRepository.findByWalletId(walletId);
        statementRepository.deleteAll(byWalletId1);
        walletRepository.delete(byWalletId.get());
        
    }

    @Synchronized
    public int getNextId() {
        Wallet wallet = walletRepository.findTopByOrderByWalletIdDesc();
        int id = (wallet != null) ? wallet.getWalletId() : 0;
        return ++id;
    }
   
}
