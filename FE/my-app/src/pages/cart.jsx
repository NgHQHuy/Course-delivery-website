import React from "react";
import "../styles/cart.css"

const Cart = () => {
    return (
        <div className="cart-container">
            <div className="cart-header">
                <span>Shopping Cart</span>
            </div>
            <div className="cart-body">
                <div className="item-list-container">
                    <div className="num-of-item">2 Courses in Cart</div>
                    <div className="item-list">
                        <div className="item-container">
                            <div className="item-thumbnail">
                                <div className="item-thumbnail-img"></div>
                            </div>
                            <div className="item-info">
                                <div className="item-overview">
                                    <div className="item-title">item title here</div>
                                    <div className="item-instructor">By ABCXYZ</div>
                                    <div className="item-rating"></div>
                                    <div className="item-number"></div>
                                </div>
                                <div className="item-btn-remove">
                                    <span>Remove</span>
                                </div>
                                <div className="item-price">444,000</div>
                            </div>
                        </div>
                    </div>

                    <div className="item-list">
                        <div className="item-container">
                            <div className="item-thumbnail">
                                <div className="item-thumbnail-img"></div>
                            </div>
                            <div className="item-info">
                                <div className="item-overview">
                                    <div className="item-title">item title here</div>
                                    <div className="item-instructor">By ABCXYZ</div>
                                    <div className="item-rating"></div>
                                    <div className="item-number"></div>
                                </div>
                                <div className="item-btn-remove">
                                    <span>Remove</span>
                                </div>
                                <div className="item-price">444,000</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="sidebar-info"></div>
            </div>
        </div>
    );
}

export default Cart;