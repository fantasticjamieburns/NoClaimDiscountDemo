pragma solidity ^0.4.18;

// Policy Issuer:      0xe51061c1EEE162ad92136315a2Ef2876bBD164E8
// Policy Holder:      0x537546bfA97E5608094463C7BF15f93D187F41F9
// Policy Start Date:  1514764800 (2018-01-01 00:00:00)
// Policy End Date:    1546300799 (2018-12-31 23:59:59)

contract NoClaimDiscount {

    address private policyIssuer;
    address private policyHolder;
    uint public policyStart;
    uint public policyEnd;

    uint public accrualFequencyInSeconds;
    uint public accrualFrom;

    event Create(address indexed policyHolder);
    event Claim(address indexed policyHolder);

    function NoClaimDiscount(
        address _policyHolder,
        uint _policyStart,
        uint _policyEnd,
        uint _accrualFequencyInSeconds,
        uint _accrualFrom
    ) public {

        // policy must not end before it started
        require(_policyEnd >= _policyStart);

        // _accrualFequencyInSeconds must be at least one
        require(_accrualFequencyInSeconds >= 0);

        // _accrualFrom must be within policy term
        require(_accrualFrom >= _policyStart);
        require(_accrualFrom <= _policyEnd);

        policyIssuer = msg.sender;
        policyHolder = _policyHolder;
        policyStart = _policyStart;
        policyEnd = _policyEnd;

        accrualFequencyInSeconds = _accrualFequencyInSeconds;
        accrualFrom = _accrualFrom;

        // Notify of create
        Create(policyHolder);
    }

    function entitlement()
      constant public returns (uint) {

        uint accrualTo = block.timestamp;
        if (accrualTo > policyEnd) {
            accrualTo = policyEnd;
        }

        uint _entitlement = 0;

        for (uint p = accrualFrom; p < accrualTo; p++) {
            p += accrualFequencyInSeconds;
            _entitlement++;
        }

        return _entitlement;
    }

    function adjustForClaim()
      public returns (bool success) {

        // Only the policyIssuer can add a claim
        require(msg.sender == policyIssuer);

        // Only policies in force can have claims added
        require(block.timestamp >= policyStart);
        require(block.timestamp <= policyEnd);

        accrualFrom = block.timestamp;

        // Notify of claim
        Claim(policyHolder);

        return true;
    }
}
