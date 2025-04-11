import React from 'react';
import {Link} from 'react-router-dom';
import potatoLogo from '../assets/potato-logo.png';

const LogoLink = ({width = "120"}) => {
    return (
        <Link to="/">
            <img
                src={potatoLogo}
                alt="감자 로고"
                className="potato-logo"
                width={width}
                style={{cursor: 'pointer'}}
            />
        </Link>
    );
};

export default LogoLink;
