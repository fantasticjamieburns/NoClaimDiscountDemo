package com.jamieburns.noclaimdiscount;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.1.1.
 */
public final class NoClaimDiscount extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60405160a08061032b83398101604052808051919060200180519190602001805191906020018051919060200180519150508383101561004e57600080fd5b600082101561005c57600080fd5b8381101561006957600080fd5b8281111561007657600080fd5b60008054600160a060020a03338116600160a060020a03199283161790925560018054888416921691909117908190556002869055600385905560048490556005839055167fe3758539c1bd6726422843471b2886c2d2cefd3b4aead6778386283e20a32a8060405160405180910390a25050505050610230806100fb6000396000f3006060604052600436106100775763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166333a729e1811461007c5780633c52ae80146100a157806343dd7e5b146100b4578063b3a69411146100db578063f71371e8146100ee578063f8a2b44614610101575b600080fd5b341561008757600080fd5b61008f610114565b60405190815260200160405180910390f35b34156100ac57600080fd5b61008f61011a565b34156100bf57600080fd5b6100c7610120565b604051901515815260200160405180910390f35b34156100e657600080fd5b61008f6101b6565b34156100f957600080fd5b61008f6101f8565b341561010c57600080fd5b61008f6101fe565b60055481565b60045481565b600080543373ffffffffffffffffffffffffffffffffffffffff90811691161461014957600080fd5b60025442101561015857600080fd5b60035442111561016757600080fd5b4260055560015473ffffffffffffffffffffffffffffffffffffffff167f0c7ef932d3b91976772937f18d5ef9b39a9930bef486b576c374f047c4b512dc60405160405180910390a250600190565b6000806000804292506003548311156101cf5760035492505b50506005546000905b828110156101f1576004546001928301929101016101d8565b5092915050565b60025481565b600354815600a165627a7a723058205619ab1b25543b4653ffe0da1724f7d9f490243812b910fbbb3ad4eaa3d3e5400029";

    private NoClaimDiscount(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private NoClaimDiscount(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<CreateEventResponse> getCreateEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Create",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList());
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<CreateEventResponse> responses = new ArrayList<CreateEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            CreateEventResponse typedResponse = new CreateEventResponse();
            typedResponse.policyHolder = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<CreateEventResponse> createEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Create",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, CreateEventResponse>() {
            @Override
            public CreateEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                CreateEventResponse typedResponse = new CreateEventResponse();
                typedResponse.policyHolder = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ClaimEventResponse> getClaimEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Claim",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList());
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<ClaimEventResponse> responses = new ArrayList<ClaimEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            ClaimEventResponse typedResponse = new ClaimEventResponse();
            typedResponse.policyHolder = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ClaimEventResponse> claimEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Claim",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList());
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ClaimEventResponse>() {
            @Override
            public ClaimEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                ClaimEventResponse typedResponse = new ClaimEventResponse();
                typedResponse.policyHolder = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<BigInteger> accrualFrom() {
        Function function = new Function("accrualFrom",
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> accrualFequencyInSeconds() {
        Function function = new Function("accrualFequencyInSeconds",
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> adjustForClaim() {
        Function function = new Function(
            "adjustForClaim",
            Arrays.<Type>asList(),
            Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> entitlement() {
        Function function = new Function("entitlement",
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> policyStart() {
        Function function = new Function("policyStart",
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> policyEnd() {
        Function function = new Function("policyEnd",
            Arrays.<Type>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<NoClaimDiscount> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _policyHolder, BigInteger _policyStart, BigInteger _policyEnd, BigInteger _accrualFequencyInSeconds, BigInteger _accrualFrom) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_policyHolder),
            new Uint256(_policyStart),
            new Uint256(_policyEnd),
            new Uint256(_accrualFequencyInSeconds),
            new Uint256(_accrualFrom)));
        return deployRemoteCall(NoClaimDiscount.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<NoClaimDiscount> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _policyHolder, BigInteger _policyStart, BigInteger _policyEnd, BigInteger _accrualFequencyInSeconds, BigInteger _accrualFrom) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_policyHolder),
            new Uint256(_policyStart),
            new Uint256(_policyEnd),
            new Uint256(_accrualFequencyInSeconds),
            new Uint256(_accrualFrom)));
        return deployRemoteCall(NoClaimDiscount.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static NoClaimDiscount load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NoClaimDiscount(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NoClaimDiscount load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NoClaimDiscount(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class CreateEventResponse {
        public String policyHolder;
    }

    public static class ClaimEventResponse {
        public String policyHolder;
    }
}
