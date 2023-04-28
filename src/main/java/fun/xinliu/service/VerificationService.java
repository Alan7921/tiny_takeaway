package fun.xinliu.service;

import fun.xinliu.common.VerificationCode;

public interface VerificationService {

    public void sendMail(String to, String subject, String text);

    public VerificationCode generateCode();

    public  boolean isValid(String receivedCode, VerificationCode savedCode);

    public  boolean isTimeOut(VerificationCode savedCode);
}
